package bgzs;

import bb.simple.ISimpleOut;
import com.intellij.ui.components.JBTextArea;

import javax.swing.*;

public class CrawlingOut implements ISimpleOut {
    private final JBTextArea jbTextArea;

    public CrawlingOut(JBTextArea jbTextArea) {
        this.jbTextArea = jbTextArea;
    }

    @Override
    public void println(String ss) {
        SwingUtilities.invokeLater(() -> jbTextArea.append(ss+"\n"));
    }

    @Override
    public void clean() {
        SwingUtilities.invokeLater(() -> jbTextArea.setText(""));
    }
}
