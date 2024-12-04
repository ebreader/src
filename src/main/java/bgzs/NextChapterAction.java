package bgzs;
import bb.book.BBook;
import bb.book.BBookException;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class NextChapterAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        BBook book=BGZS.getReader().getReadingBook();
        if(book!=null){
            book.nextChapter();
            PP.show(BGZS.getReader().getReadingBook().currentText());
        }
    }
}
