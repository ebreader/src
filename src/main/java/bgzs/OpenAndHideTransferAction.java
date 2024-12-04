package bgzs;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

public class OpenAndHideTransferAction extends SubtleBookAction {
    private static String transferURL;
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        ToolWindow toolWindow = ToolWindowManager.getInstance(anActionEvent.getProject()).getToolWindow("Transfer");
        if (toolWindow != null) {
            if(toolWindow.isVisible()){
                toolWindow.hide();
            }else{
                toolWindow.show();
                if(transferURL==null){
                    transferURL=BGZS.getReader().getTransfer();
                    TransferBrowserFactory.browser.loadURL(transferURL);
                }else if(!transferURL.equals(BGZS.getReader().getTransfer())){
                    transferURL=BGZS.getReader().getTransfer();
                    TransferBrowserFactory.browser.loadURL(transferURL);
                }
            }
        }
    }
}
