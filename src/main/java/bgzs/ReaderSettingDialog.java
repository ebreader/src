package bgzs;
import bb.book.BBookException;
import com.intellij.openapi.project.Project;

public class ReaderSettingDialog extends SettingDialog{
    public ReaderSettingDialog(Project project) throws BBookException {
        super(project);
        addInput("l",BGZS.getString("cpl"));
        addInput("c",BGZS.getString("charSet"));
        addInput("e",BGZS.getString("searchEngine"));
        addInput("t",BGZS.getString("transfer"));
        addInput("a",BGZS.getString("AI"));
        init();
    }

    @Override
    protected void handleCustomOkAction() throws Exception {
        String l=get("l");
        String c=get("c");
        String e=get("e");
        String t=get("t");
        String a=get("a");
        String p=get("p");
        BGZS.getReader().setCountPerLine(Integer.parseInt(l),false);
        BGZS.getReader().setCharSet(c,false);
        BGZS.getReader().setSearchEngine(e,false);
        BGZS.getReader().setAI(a,false);
        BGZS.getReader().setTransfer(t,false);
        BGZS.getReader().saveSetting();
        this.close(0);
    }
}
