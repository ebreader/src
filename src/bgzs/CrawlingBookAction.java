package bgzs;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;


public class CrawlingBookAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        ToolWindow toolWindow = ToolWindowManager.getInstance(anActionEvent.getProject()).getToolWindow("Crawler");
        if (toolWindow != null) {
            if(toolWindow.isVisible()){
                toolWindow.hide();
            }else{
                toolWindow.show();
            }
        }
    }
}