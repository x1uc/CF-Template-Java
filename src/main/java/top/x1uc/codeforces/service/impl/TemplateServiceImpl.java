package top.x1uc.codeforces.service.impl;

import com.alibaba.fastjson2.JSON;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import top.x1uc.codeforces.notify.NotificationUtil;
import top.x1uc.codeforces.pojo.ProblemTemplate;
import top.x1uc.codeforces.service.TemplateService;
import top.x1uc.codeforces.storage.CodeForcesTemplateGlobalSettings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateServiceImpl implements TemplateService {
    private ServerSocket serverSocket;
    private volatile boolean running = false;

    @Override
    public void startServer() {
        if (running) {
            System.out.println("Server is already running");
            return;
        }
        try {
            CodeForcesTemplateGlobalSettings settings = new CodeForcesTemplateGlobalSettings();
            serverSocket = new ServerSocket(settings.getCphPort()); 
            running = true;
            new Thread(this::handleConnections).start();
            System.out.println("Server started on port " + settings.getCphPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnections() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleRequest(Socket clientSocket) {
        // parse client info
        String problemInfo = getProblemInfo(clientSocket);
        if (problemInfo == null) { return; }
        ProblemTemplate problemTemplate = JSON.parseObject(problemInfo, ProblemTemplate.class);
        // generateFile
        generateFile(problemTemplate);
    }

    private String getProblemInfo(Socket clientSocket) {
        try (InputStream in = clientSocket.getInputStream(); OutputStream out = clientSocket.getOutputStream()) {

            // get param & parse
            byte[] buffer = new byte[2048];
            int bytesRead = in.read(buffer);
            String request = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            String body = request.split("\\r\\n\\r\\n")[1];
            // response client 
            String response = "HTTP/1.1 200 OK\r\n\r\n";
            out.write(response.getBytes(StandardCharsets.UTF_8));
            
            // return body
            return body;
        } catch (IOException e) {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            NotificationUtil.showNotification(project, "Error", "parse RequestInfo error", NotificationType.ERROR);
        }
        return null;
    }

    private void generateFile(ProblemTemplate problemTemplate) {
        
        CodeForcesTemplateGlobalSettings globalSettings = CodeForcesTemplateGlobalSettings.getInstance();
        String fileName = parseFileName(problemTemplate.getUrl());
        
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        String filePath = project.getBasePath() + File.separator + fileName + ".java";
        String TemplatePath = globalSettings.getTemplatePath();
        File templateFile = new File(TemplatePath);
        try {
            File file = new File(filePath);
            if (!templateFile.exists()) {
                NotificationUtil.showNotification(project, "Error", "The template file does not exist", NotificationType.ERROR);
                return;
            }
            if (file.exists()) {
                NotificationUtil.showNotification(project, "Warn", "file already exists", NotificationType.WARNING);
                return;
            }
            if (file.createNewFile()) {
                try (FileWriter writer = new FileWriter(file)) {
                    String content = generateClassContent(problemTemplate); 
                    writer.write(content);
                    writer.flush();
                    NotificationUtil.showNotification(project, "Notify", fileName + " success generation", NotificationType.INFORMATION);
                }
            } else {
                NotificationUtil.showNotification(project, "Error", "Fail to create file", NotificationType.ERROR);
            }
        } catch (IOException e) {
            NotificationUtil.showNotification(project, "Error", "The template file does not exist", NotificationType.ERROR);
        }
    }


    /**
     * @param problemTemplate problemTemplate
     * @return The contents of the generated file
     * @throws IOException
     */
    private String generateClassContent(ProblemTemplate problemTemplate) throws IOException {
        //get information
        String className = parseFileName(problemTemplate.getUrl());
        CodeForcesTemplateGlobalSettings globalSettings = CodeForcesTemplateGlobalSettings.getInstance();
        String TemplatePath = globalSettings.getTemplatePath();
         // read template
        BufferedReader reader = new BufferedReader(new FileReader(TemplatePath));
        StringBuilder content = new StringBuilder();
        String line;
        
        // generate file 
        Pattern classPattern = Pattern.compile("public\\s+class\\s+(\\w+)");
        while ((line = reader.readLine()) != null) {
            Matcher matcher = classPattern.matcher(line);
            if (matcher.find()) {
                // replace class name
                line = line.replace(matcher.group(1), className);
            }
            content.append(line).append("\n");
        }
        reader.close();
        // Problem information annotation (head of file)
        String headerComment = String.format(
                "/**\n" +
                        " * Name: %s\n" +
                        " * Group: %s\n" +
                        " * URL: %s\n" +
                        " * Memory Limit: %d\n" +
                        " * Time Limit: %d\n" +
                        " */\n",
                problemTemplate.getName(),
                problemTemplate.getGroup(),
                problemTemplate.getUrl(),
                problemTemplate.getMemoryLimit(),
                problemTemplate.getTimeLimit()
        );
        content.insert(0, headerComment);
        return content.toString();
    }


    /**
     * @param requestUrl example : "<a href="https://codeforces.com/problemset/problem/1774/C">...</a>"
     * @return P1774C
     */
    private String parseFileName(String requestUrl) {
        String[] fileName = requestUrl.split("/");
        return 'P' + fileName[fileName.length - 2] + fileName[fileName.length - 1];
    }

    @Override
    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            System.out.println("Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isServerRunning() {
        return running;
    }

}
