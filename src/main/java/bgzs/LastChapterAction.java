package bgzs;
import bb.book.BBook;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class LastChapterAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        BBook book=BGZS.getReader().getReadingBook();
        if(book!=null){
            book.lastChapter();
            PP.show(BGZS.getReader().getReadingBook().currentText());
        }
    }
}
