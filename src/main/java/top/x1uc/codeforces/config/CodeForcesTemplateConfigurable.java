package top.x1uc.codeforces.config;

import com.intellij.openapi.options.Configurable;
import top.x1uc.codeforces.gui.config.ConfigUI;
import top.x1uc.codeforces.storage.CodeForcesTemplateGlobalSettings;

import javax.swing.*;

public class CodeForcesTemplateConfigurable implements Configurable {
    private ConfigUI settingsForm;

    @Override
    public String getDisplayName() {
        return "CodeForces Template Java";
    }

    @Override
    public JComponent createComponent() {
        settingsForm = new ConfigUI();
        return settingsForm.getPanel();
    }

    // Determine whether it is modified
    @Override
    public boolean isModified() {
        CodeForcesTemplateGlobalSettings settings = CodeForcesTemplateGlobalSettings.getInstance();
        
        String currentPath = settingsForm.getTemplatePath().getText();
        String currentPortText = settingsForm.getCphPort().getText();
        
        boolean isPathModified = !currentPath.equals(settings.getTemplatePath());
        
        Integer currentPort = null;
        try {
            if (!currentPortText.isEmpty()) {
                currentPort = Integer.valueOf(currentPortText);
            }
        } catch (NumberFormatException e) {
            return true;
        }
        assert currentPort != null;
        boolean isPortModified = !currentPort.equals(settings.getCphPort());

        return isPathModified || isPortModified;
    }

    @Override
    public void apply() {
        String newPath = settingsForm.getTemplatePath().getText();
        String newPortText = settingsForm.getCphPort().getText();
        
        Integer newPort = null;
        try {
            if (!newPortText.isEmpty()) {
                newPort = Integer.valueOf(newPortText);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(settingsForm.getPanel(), "Invalid port number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        CodeForcesTemplateGlobalSettings settings = CodeForcesTemplateGlobalSettings.getInstance();
        settings.setTemplatePath(newPath);
        settings.setCphPort(newPort);
    }

    @Override
    public void reset() {
        CodeForcesTemplateGlobalSettings settings = CodeForcesTemplateGlobalSettings.getInstance();
        settingsForm.getTemplatePath().setText(settings.getTemplatePath());

        Integer port = settings.getCphPort();
        settingsForm.getCphPort().setText(port != null ? port.toString() : "");
    }

    @Override
    public void disposeUIResources() {
        settingsForm = null;
    }
}
