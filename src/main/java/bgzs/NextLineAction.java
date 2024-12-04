package bgzs;
import bb.book.BBook;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;

public class NextLineAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        BBook book=BGZS.getReader().getReadingBook();
        if(book!=null){
            PP.show(BGZS.getReader().getReadingBook().nextText());
        }
    }
}
