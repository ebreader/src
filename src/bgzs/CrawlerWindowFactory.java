package bgzs;

import bb.book.BBSymbol;
import bb.book.BBook;
import bb.book.BBookDescriptor;
import bb.book.BBookReader;
import bb.simple.SimpleException;
import bb.simple.SimpleFileSave;
import bb.simple.StringTool;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import bb.crawl.*;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CrawlerWindowFactory implements ToolWindowFactory {
    private JBTextField crawlingURL;
    private JBTextField basicURL;
    private JBTextField titleSelector;
    private JBTextField chapterSelector;
    private JBTextField contentSelector;
    private JBTextArea area;
    private Project project;
    private ToolWindow toolWindow;
    private final JButton crawlingBtn=new JButton();
    private final JButton saveBtn=new JButton();
    private final JButton loadBtn=new JButton();
    private final JButton exportBtn=new JButton();
    private final Crawler crawler=new Crawler();
    private final CrawlerDescriptor descr=new CrawlerDescriptor();
    private CrawlBook cbk;

    private void load(ActionEvent anAction){
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        VirtualFile[] files = FileChooser.chooseFiles(descriptor,project, null);

        if (files.length <= 0) {
            return;
        }
        String filePath = files[0].getPath();
        Properties properties=new Properties();
        Reader inputStream=null;
        try{
            inputStream=new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8);
            properties.load(inputStream);
            descr.init(properties);
            crawlingURL.setText(descr.getCrawlingURL());
            basicURL.setText(descr.getBasicURL());
            titleSelector.setText(descr.getTitleSelector());
            chapterSelector.setText(descr.getChapterSelector());
            contentSelector.setText(descr.getContentSelector());
        }catch (Exception e){

        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }
    }
    private void save(ActionEvent anAction){
        if(cbk==null){
            crawler.cleanOut();
            crawler.println("no crawling ebook");
           return;
        }
        try {
            disableAllBtn();
            BBookDescriptor descriptor=new BBookDescriptor(BGZS.getReader(),BGZS.getReader().generateID(descr.getCrawlingURL()+descr.getTitleSelector()+descr.getChapterSelector()+descr.getContentSelector()));
            BBook book=new BBook(descriptor);
            book.setTitle(cbk.getTitle());
            for(CrawlChapter cc:cbk.getChapterList()){
                book.createChapter(cc.getContentList(),cc.getName());
            }
            book.save();
            area.setText("save crawling book "+book.getTitle()+" success");
        } catch (SimpleException e) {
            crawler.println(e.msg);
        }finally {
            cbk=null;
            enableAllBtn();
        }
    }
    private void doImportBook(String toDir,String select){
        try{

            if(select.startsWith(BBSymbol.MAGIC)){
                String[] sm=select.split(BBSymbol.BR);
                String bkName=null;
                for(int i=0;i<sm.length;i++){
                    String st=sm[i];
                    if(st.startsWith(BBSymbol.TITLE_PREFIX)){
                        bkName=st.replace(BBSymbol.TITLE_PREFIX,"");
                        break;
                    }
                }

                crawler.cleanOut();
                String toFile=toDir+ File.separator+bkName+".txt";
                SimpleFileSave.writeData(toFile,select);
                crawler.println("export "+bkName+" to file "+toFile);
                return;
            }


            java.util.List<BBookDescriptor> bdlist= BGZS.getReader().getBookDescriptorList();
            if(bdlist.isEmpty()){
                crawler.println(BGZS.getString("historyBookEmpty"));
                return;
            }
            String[] sm=select.split(BBSymbol.BR);
            crawler.println("");
            for(BBookDescriptor bBookDescriptor:bdlist){
                boolean hit=false;
                for(String s:sm){
                    if(bBookDescriptor.getTitle().contains(s)){
                        hit=true;
                        break;
                    }

                }
                if(!hit){
                    continue;
                }


                BBook book=new BBook(bBookDescriptor);
                book.loadChapter();
                book.resetChapterName();
                String toFile=toDir+ File.separator+book.getTitle()+".txt";
                SimpleFileSave.writeData(toFile,book.toText());
                crawler.println("export "+book.getTitle()+" to file "+toFile);
            }
        }catch (SimpleException e){
            crawler.println(e.msg);
        }catch (Exception e){
            crawler.println(StringTool.exceptionStack(e));
        }finally {
            enableAllBtn();
        }
    }
    private void importAllBook(ActionEvent anAction){
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile[] files = FileChooser.chooseFiles(descriptor,project, null);

        if (files.length <= 0) {
            return;
        }
        String filePath = files[0].getPath();


        String select=area.getText().trim();
        disableAllBtn();

        ProgressManager.getInstance().run(new Task.Backgroundable(project,"importing") {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                doImportBook(filePath,select);
            }
        });

    }

    private void crawling(ActionEvent anAction){
        cbk=null;
        crawler.cleanOut();
        descr.setCrawlingURL(crawlingURL.getText().trim());
        descr.setBasicURL(basicURL.getText().trim());
        descr.setTitleSelector(titleSelector.getText().trim());
        descr.setChapterSelector(chapterSelector.getText().trim());
        descr.setContentSelector(contentSelector.getText().trim());
        try {
            BBookReader reader=BGZS.getReader();

            reader.set("crawlingURL",descr.getCrawlingURL());
            reader.set("basicURL",descr.getBasicURL());
            reader.set("titleSelector",descr.getTitleSelector());
            reader.set("chapterSelector",descr.getChapterSelector());
            reader.set("contentSelector",descr.getContentSelector());
            reader.saveSetting();
            crawler.init(descr);
        } catch (SimpleException e) {
            crawler.println(e.msg);
            return;
        }
        disableAllBtn();
        ProgressManager.getInstance().run(new Task.Backgroundable(project,"Crawling "+descr.getName()) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                runBack(crawler);
            }
        });
    }
    private void disableAllBtn(){
        crawlingBtn.setEnabled(false);
        saveBtn.setEnabled(false);
        loadBtn.setEnabled(false);
        exportBtn.setEnabled(false);
    }
    private void enableAllBtn(){
        exportBtn.setEnabled(true);
        crawlingBtn.setEnabled(true);
        saveBtn.setEnabled(true);
        loadBtn.setEnabled(true);
    }

    private void runBack(Crawler crawler){
        try {
            cbk=crawler.crawling();
            SwingUtilities.invokeLater(() -> updateUI(cbk));
        } catch (SimpleException e) {
            crawler.println(e.msg);
        }
    }
    private void updateUI(CrawlBook bk){
        area.setText(bk.toText());
        enableAllBtn();
    }
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project=project;
        this.toolWindow=toolWindow;
        try{
            JPanel mainPanel=new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

            panel.add(new JLabel(BGZS.getString("crawlingURL")));
            crawlingURL = new JBTextField();
            panel.add(crawlingURL);


            panel.add(new JLabel(BGZS.getString("basicURL")));
            basicURL = new JBTextField();
            panel.add(basicURL);


            panel.add(new JLabel(BGZS.getString("titleSelector")));
            titleSelector = new JBTextField();
            panel.add(titleSelector);


            panel.add(new JLabel(BGZS.getString("chapterSelector")));
            chapterSelector = new JBTextField();
            panel.add(chapterSelector);


            panel.add(new JLabel(BGZS.getString("contentSelector")));
            contentSelector = new JBTextField();
            panel.add(contentSelector);
            mainPanel.add(panel,BorderLayout.NORTH);


            panel=new JPanel(new FlowLayout());
            crawlingBtn.setText(BGZS.getString("crawlingBtn"));
            crawlingBtn.addActionListener(this::crawling);
            panel.add(crawlingBtn);



            saveBtn.setText(BGZS.getString("saveBtn"));
            saveBtn.addActionListener(this::save);
            panel.add(saveBtn);


            loadBtn.setText(BGZS.getString("loadBtn"));
            loadBtn.addActionListener(this::load);
            panel.add(loadBtn);

            exportBtn.setText(BGZS.getString("importBtn"));
            exportBtn.addActionListener(this::importAllBook);
            panel.add(exportBtn);

            mainPanel.add(panel,BorderLayout.SOUTH);

            area = new JBTextArea();
            area.setCaret(new DefaultCaret() {
                @Override
                public void setSelectionVisible(boolean visible) {
                    super.setSelectionVisible(visible);
                }
            });
            area.setLineWrap(true);
            mainPanel.add(new JScrollPane(area), BorderLayout.CENTER);


            BBookReader reader=BGZS.getReader();
            crawlingURL.setText(reader.get("crawlingURL"));
            basicURL.setText(reader.get("basicURL"));
            titleSelector.setText(reader.get("titleSelector"));
            chapterSelector.setText(reader.get("chapterSelector"));
            contentSelector.setText(reader.get("contentSelector"));


            crawler.setOut(new CrawlingOut(area));
            toolWindow.getContentManager().addContent(
                    toolWindow.getContentManager().getFactory().createContent(mainPanel,"",false)
            );

        }catch (SimpleException e){
            PP.show(e.msg);
        }catch (Exception e){
            PP.show(StringTool.exceptionStack(e));
        }
    }

}
