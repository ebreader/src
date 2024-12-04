package bgzs;

import com.intellij.openapi.actionSystem.AnActionEvent;

public class CleanAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        PP.clean();
    }
}
