package bgzs;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

public class OpenAndHideSearchEngineAction extends SubtleBookAction {
    private static String engineURL;
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        ToolWindow toolWindow = ToolWindowManager.getInstance(anActionEvent.getProject()).getToolWindow("Search");
        if (toolWindow != null) {
            if(toolWindow.isVisible()){
                toolWindow.hide();
            }else{
                toolWindow.show();
                if(engineURL==null){
                    engineURL=BGZS.getReader().getSearchEngine();
                    EngineBrowserFactory.browser.loadURL(engineURL);
                }else if(!engineURL.equals(BGZS.getReader().getSearchEngine())){
                    engineURL=BGZS.getReader().getSearchEngine();
                    EngineBrowserFactory.browser.loadURL(engineURL);
                }
            }
        }
    }
}
