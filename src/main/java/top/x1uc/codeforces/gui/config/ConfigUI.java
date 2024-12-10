package top.x1uc.codeforces.gui.config;

import lombok.Data;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Data
public class ConfigUI {
    private JPanel panel;
    private JTextField cphPort;
    private JTextField templatePath;
    private JLabel portLabel;
    private JLabel pathLabel;

    public ConfigUI() {
        // 初始化面板
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 初始化端口标签和输入框
        portLabel = new JLabel("Port:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(portLabel, gbc);

        cphPort = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel.add(cphPort, gbc);

        // 初始化路径标签和输入框
        pathLabel = new JLabel("Template Path:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(pathLabel, gbc);

        templatePath = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panel.add(templatePath, gbc);
    }

    public void setCphPort(String port) {
        cphPort.setText(port);
    }

    public void setTemplatePath(String path) {
        templatePath.setText(path);
    }
}
