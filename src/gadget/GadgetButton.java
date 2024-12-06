package gadget;

import bb.book.BBookException;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class GadgetButton extends JButton implements ActionListener {
    protected JBTextArea resultArea;
    protected JBTextField input;
    @Override
    public void actionPerformed(ActionEvent e) {
        String inputText=input.getText().trim();
        String result;
        try {
            result=executeFunction(inputText);
        } catch (BBookException ex) {
            result=ex.msg;
        }
        resultArea.setText(result);

    }
    protected abstract String executeFunction(String inputText) throws BBookException;
}
