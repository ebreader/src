package bgzs;

import bb.book.BBookException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.jcef.JBCefBrowser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AIBrowserFactory implements ToolWindowFactory {
    static JBCefBrowser browser;
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        browser=BGZS.createBrowser();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(browser.getComponent(), BorderLayout.CENTER);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = null;
        toolWindow.setTitleActions(createToolWindowActions());
        content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }


     private List<AnAction> createToolWindowActions() {
         Icon icon= IconLoader.getIcon("/icons/refresh.svg",getClass());
         AnAction customAction = new AnAction("Reload", "Reload the AI", icon) {
             @Override
             public void actionPerformed(@NotNull AnActionEvent e) {
                 try {
                     browser.getCefBrowser().loadURL(BGZS.getReader().getAI());
                 } catch (BBookException ex) {
                     PP.show(ex.msg);
                 }
             }
         };

         icon= IconLoader.getIcon("/icons/forward.svg",getClass());

         AnAction forward = new AnAction("Forward", "Move to forward", icon) {
             @Override
             public void actionPerformed(@NotNull AnActionEvent e) {
                 if(browser.getCefBrowser().canGoForward()){
                     browser.getCefBrowser().goForward();
                 }
             }
         };

         icon= IconLoader.getIcon("/icons/back.svg",getClass());
         AnAction back = new AnAction("Back", "Move to back", icon) {
             @Override
             public void actionPerformed(@NotNull AnActionEvent e) {
                 if(browser.getCefBrowser().canGoBack()){
                     browser.getCefBrowser().goBack();
                 }
             }
         };


         List<AnAction> list= new ArrayList<>();
         list.add(customAction);
         list.add(back);
         list.add(forward);
         return list;
    }

}
