package bgzs;
import bb.book.BBook;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class SetChapterRegexAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        BBook book=BGZS.getReader().getReadingBook();
        if(book==null){
            PP.show(BGZS.getString("selectBookFirst"));
            return;
        }
        RegexIndexChapterDialog mm=new RegexIndexChapterDialog(anActionEvent.getProject());

        if(book.getChapterRegex()!=null&& !book.getChapterRegex().isEmpty()){
            mm.set("l",book.getChapterRegex());
        }else{
            mm.set("l",BGZS.getString("defaultChapterRegex"));
        }
        mm.setTitle(BGZS.getString("buildChapterTitle")+"->"+book.getTitle());
        mm.show();
    }
}
