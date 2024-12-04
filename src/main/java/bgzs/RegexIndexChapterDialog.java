package bgzs;

import bb.book.BBookException;
import com.intellij.openapi.project.Project;

public class RegexIndexChapterDialog extends SettingDialog{
    public RegexIndexChapterDialog(Project project) throws BBookException {
        super(project);
        addInput("l",BGZS.getString("regex"));
        init();
    }
    @Override
    protected void handleCustomOkAction() throws Exception {
        if(BGZS.getReader().getReadingBook()==null){
            PP.show(BGZS.getString("selectBookFirst"));
            return;
        }
        String regex=get("l");
        if(BGZS.getReader().getReadingBook().setChapterRegex(regex)){
            PP.show(BGZS.getReader().getReadingBook().currentText());
        }else{
            PP.show(BGZS.getString("noindc").replace("#",regex));
        }
        this.close(0);
    }
}
