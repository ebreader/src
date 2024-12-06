package bgzs;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class SettingAction extends SubtleBookAction {
    @Override
    protected void exec(AnActionEvent anActionEvent) throws Exception {
        ReaderSettingDialog mm=new ReaderSettingDialog(anActionEvent.getProject());
        mm.set("l", BGZS.getReader().getCountPerLine()+"");
        mm.set("c",BGZS.getReader().getCharSet());

        mm.setTitle(BGZS.getString("settingTitle"));
        mm.show();

    }
}
