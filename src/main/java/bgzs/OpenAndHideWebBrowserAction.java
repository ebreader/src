package bgzs;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

public class OpenAndHideWebBrowserAction extends SubtleBookAction {
    private static String engineURL;
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        ToolWindow toolWindow = ToolWindowManager.getInstance(anActionEvent.getProject()).getToolWindow("Browser");
        if (toolWindow != null) {
            if(toolWindow.isVisible()){
                toolWindow.hide();
            }else{
                toolWindow.show();
            }
        }
    }
}
