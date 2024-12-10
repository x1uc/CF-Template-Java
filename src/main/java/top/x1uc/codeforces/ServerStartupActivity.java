package top.x1uc.codeforces;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.x1uc.codeforces.service.TemplateService;

public class ServerStartupActivity implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        TemplateService serverService = ApplicationManager.getApplication().getService(TemplateService.class);
        serverService.startServer();
        return Unit.INSTANCE;
    }
}
