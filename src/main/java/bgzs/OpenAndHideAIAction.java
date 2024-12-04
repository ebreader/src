package bgzs;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
public class OpenAndHideAIAction extends SubtleBookAction {
    private static String aiURL;
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        ToolWindow toolWindow = ToolWindowManager.getInstance(anActionEvent.getProject()).getToolWindow("AI");
        if (toolWindow != null) {
            if(toolWindow.isVisible()){
                toolWindow.hide();
            }else{
                toolWindow.show();
                if(aiURL==null){
                    aiURL=BGZS.getReader().getAI();
                    AIBrowserFactory.browser.loadURL(aiURL);
                }else if(!aiURL.equals(BGZS.getReader().getAI())){
                    aiURL=BGZS.getReader().getAI();
                    AIBrowserFactory.browser.loadURL(aiURL);
                }
            }
        }
    }
}
