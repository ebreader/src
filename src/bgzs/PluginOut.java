package bgzs;

import bb.simple.ISimpleOut;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PluginOut implements ISimpleOut {
    private final List<Notification> list=new ArrayList<>();
    public void clean(){
        for (Notification notification : list) {
            notification.expire();
        }
        list.clear();
        Random random=new Random();
        for(int i=0;i<20;i++){
            println("Build completed successfully in "+(random.nextInt(10)+5)+" sec, "+(random.nextInt(1000)+200)+" ms");
            println("All file are up-to-date");
        }
    }


    @Override
    public void println(String msg) {
        Notification no = new Notification("myl", "", msg, NotificationType.INFORMATION);
        for(Project project: ProjectManager.getInstance().getOpenProjects()){
            Notifications.Bus.notify(no,project);
        }
        list.add(no);
    }
}
