package gadget;

import bb.book.BBookException;
import bgzs.BGZS;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class GadgetToolWindowFactory implements ToolWindowFactory {
    private JBTextField input;
    private JBTextArea area;
    private JPanel buttonListPanel;
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout());
        input=new JBTextField();
        input.setPreferredSize(new Dimension(0,30));
        mainPanel.add(input,BorderLayout.NORTH);

        area = new JBTextArea();
        area.setCaret(new DefaultCaret() {
            @Override
            public void setSelectionVisible(boolean visible) {
                super.setSelectionVisible(visible);
            }
        });
        area.setLineWrap(true);
        mainPanel.add(new JScrollPane(area), BorderLayout.CENTER);

        buttonListPanel = new JPanel();
        buttonListPanel.setLayout(new BoxLayout(buttonListPanel, BoxLayout.Y_AXIS));


        registerFunction();


        JBScrollPane scrollPane = new JBScrollPane(buttonListPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.WEST);
        toolWindow.getContentManager().addContent(
                toolWindow.getContentManager().getFactory().createContent(mainPanel,"",false)
        );
    }
    private void registerFunction(){
        addFunction("timestamp",new TimestampButton());
        addFunction("messageDigest",new MessageDigestButton());
        addFunction("b64txt",new Base64Button());
        addFunction("urltxt",new URLButton());

        addFunction("jsonformat",new JSONFormatButton());
    }


    private void addFunction(String textKey,GadgetButton button){
        button.resultArea=area;
        button.input=input;
        try {
            button.setText(BGZS.getString(textKey));
        } catch (BBookException e) {
            button.setText(textKey);
        }
        button.addActionListener(button);
        buttonListPanel.add(button);
    }

}
