package bgzs;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
public class OpenLocalFileAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        VirtualFile[] files = FileChooser.chooseFiles(descriptor,anActionEvent.getProject(), null);

        if (files.length <= 0) {
            return;
        }
        String filePath = files[0].getPath();
        PP.show("open file "+filePath+" ...");
        BGZS.open(filePath);
    }
}
