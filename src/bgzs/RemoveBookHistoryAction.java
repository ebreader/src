package bgzs;
import bb.book.BBookDescriptor;
import bb.book.BBookException;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import java.util.List;

public class RemoveBookHistoryAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        List<BBookDescriptor> list= BGZS.getReader().getBookDescriptorList();
        if(list.size()==0){
            PP.show(BGZS.getString("historyBookEmpty"));
            return;
        }

        // 创建弹出菜单
        JBPopupFactory.getInstance()
                .createPopupChooserBuilder(list)
                .setTitle(BGZS.getString("removeHistoryBookList"))
                .setItemChosenCallback(selectedValue -> {
                    try {
                        BGZS.getReader().removeBook(selectedValue);
                        PP.show(BGZS.getString("removeSuccess").replace("#",selectedValue.getTitle()));
                    } catch (BBookException e) {
                        PP.show(e.msg);
                    }
                })
                .createPopup()
                .showInFocusCenter();
    }
}
