package bgzs;

import bb.book.BBookException;
import bb.simple.StringTool;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public abstract class SubtleBookAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        try {
            exec(anActionEvent);
        } catch (BBookException e) {
            PP.show(e.msg);
        }catch (Exception e){
            PP.show(StringTool.exceptionStack(e));
        }
    }
    protected abstract void exec(AnActionEvent anActionEvent) throws Exception;
}
