package top.x1uc.codeforces.storage;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@State(name = "CodeForcesTemplateGlobalSettings", storages = @Storage("CodeForcesTemplateGlobalSettings.xml"))
public class CodeForcesTemplateGlobalSettings implements PersistentStateComponent<CodeForcesTemplateGlobalSettings> {
    private String templatePath = "./example.txt";
    private Integer CphPort = 12345;
    public static CodeForcesTemplateGlobalSettings getInstance() {
        return ServiceManager.getService(CodeForcesTemplateGlobalSettings.class);
    }
    
    @Override
    public CodeForcesTemplateGlobalSettings getState() {
        return this;
    }

    @Override
    public void loadState(CodeForcesTemplateGlobalSettings state) {
        this.templatePath = state.templatePath;
        this.CphPort = state.CphPort;
    }
}
