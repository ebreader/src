package bgzs;
import bb.book.BBookException;
import bb.book.BChapter;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import java.util.ArrayList;
import java.util.List;

public class ListChapterAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        if(BGZS.getReader().getReadingBook()==null){
            PP.show(BGZS.getString("selectBookFirst"));
            return;
        }
        List<BChapter> list= BGZS.getReader().getReadingBook().getChapters();
        List<BChapter> orderList=new ArrayList<>();
        int po=BGZS.getReader().getReadingBook().getChapterPointer();
        for(int i=0;i<list.size();i++){
            orderList.add(list.get(po));
            po++;
            if(po>=list.size()){
                po=0;
            }
        }

        // 创建弹出菜单
        JBPopupFactory.getInstance()
                .createPopupChooserBuilder(orderList)
                .setTitle(BGZS.getString("chapterMenu")+"("+list.size()+")")
                .setItemChosenCallback(selectedValue -> {
                    try {
                        selectedValue.locate();
                        PP.show(BGZS.getReader().getReadingBook().currentText());
                    } catch (BBookException e) {
                        PP.show(e.msg);
                    }
                })
                .createPopup()
                .showInFocusCenter();
    }
}
