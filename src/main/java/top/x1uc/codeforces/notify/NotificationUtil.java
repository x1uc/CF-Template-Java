package top.x1uc.codeforces.notify;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class NotificationUtil {

    public static void showNotification(Project project, String title, String content,NotificationType type) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("CodeForces Notify") 
                .createNotification(title, content, type)
                .notify(project);
    }
}
