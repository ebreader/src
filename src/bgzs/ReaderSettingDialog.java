package bgzs;
import bb.book.BBookException;
import com.intellij.openapi.project.Project;

public class ReaderSettingDialog extends SettingDialog{
    public ReaderSettingDialog(Project project) throws BBookException {
        super(project);
        addInput("l",BGZS.getString("cpl"));
        addInput("c",BGZS.getString("charSet"));
        init();
    }

    @Override
    protected void handleCustomOkAction() throws Exception {
        String l=get("l");
        String c=get("c");
        BGZS.getReader().setCountPerLine(Integer.parseInt(l),false);
        BGZS.getReader().setCharSet(c,false);
        BGZS.getReader().saveSetting();
        this.close(0);
    }
}
