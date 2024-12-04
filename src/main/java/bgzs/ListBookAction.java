package bgzs;
import bb.book.BBookDescriptor;
import bb.book.BBookException;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import java.util.List;

public class ListBookAction extends SubtleBookAction {
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
                .setTitle(BGZS.getString("historyBookList"))
                .setItemChosenCallback(selectedValue -> {
                    try {
                        BGZS.getReader().continueReadBook(selectedValue);
                        PP.show(BGZS.getReader().getReadingBook().currentText());
                    } catch (BBookException e) {
                        PP.show(e.msg);
                    }
                })
                .createPopup()
                .showInFocusCenter();
    }
}
