package bgzs;
import bb.book.BBookException;
import bb.simple.StringTool;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class SettingDialog extends DialogWrapper {
    protected final Map<String,String> m=new HashMap<>();
    protected final java.util.List<String> klist=new ArrayList<>();
    protected final Map<String,JBTextField> map=new HashMap<>();
    protected Project project;
    public SettingDialog(Project project) {
        super(project);
        this.project=project;
    }
    public void addInput(String key,String name){
        m.put(key,name);
        klist.add(key);
    }
    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(m.size(), 2, 10, 10));
        for(String kk:klist){
            String kv=m.get(kk);

        // 创建标签和输入框
        panel.add(new JLabel(kv));
        JBTextField textField1 = new JBTextField();
        panel.add(textField1);
        map.put(kk,textField1);
        }
        return panel; // 返回面板
    }
    protected void doOKAction(){
        try {
            handleCustomOkAction();
        } catch (BBookException e){
            PP.show(e.msg);
        }catch (Exception e) {
            PP.show(StringTool.exceptionStack(e));
        }
    }
    public void set(String key,String value){
        JBTextField jb=map.get(key);
        if(jb!=null){
            jb.setText(value);
            jb.repaint();
        }
    }
    protected abstract void handleCustomOkAction() throws Exception;
    public String get(String key){
        JBTextField jb=map.get(key);
        if(jb==null){
            return null;
        }
        return jb.getText().trim();
    }
}
