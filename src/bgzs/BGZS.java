package bgzs;

import bb.book.BBook;
import bb.book.BBookException;
import bb.book.BBookReader;
import bb.simple.I18nText;
import bb.simple.SimpleSave;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.*;
import org.cef.misc.BoolRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.cef.network.CefURLRequest;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class BGZS {
    private static BBookReader reader;
    private static I18nText i18nText;
    static {
        try {
            init();
        } catch (BBookException e) {
            PP.show(e.msg);
        }
    }


    private static void init() throws BBookException {
         if(reader==null){
            String ppsh=System.getProperty("user.home")+ File.separator+"_bb_bgzs_";
            File file=new File(ppsh);
            if(file.exists()&&file.isFile()){
                throw new BBookException("the SubtleBook need dir "+file.getPath()+",but the file is exist.please resolve this problem");
            }else if(!file.exists()){
                if(!file.mkdirs()){
                    throw new BBookException("the SubtleBook need dir "+file.getPath()+" but create fail.please resolve this problem");
                }
            }
            reader=new BBookReader(file.getPath());
            String language= reader.getLanguage();
            if(language==null){
                i18nText=new BGZSI18nText(ResourceBundle.getBundle("MessagesBundle"));
            }else{
                ResourceBundle resourceBundle=ResourceBundle.getBundle("MessagesBundle",Locale.forLanguageTag(language));
                if(resourceBundle==null){
                    i18nText=new BGZSI18nText(ResourceBundle.getBundle("MessagesBundle"));
                }else{
                    i18nText=new BGZSI18nText(resourceBundle);
                }
            }
            reader.setI18n(i18nText);


        }
    }
    public static void resetProxy(){
        String proxy=reader.getProxy();
        String[] ss=new String[]{"",""};
        if(proxy!=null&&!proxy.isEmpty()){
            if(proxy.startsWith("http://")){
                proxy=proxy.replace("http://","");
            }
            if(proxy.startsWith("https://")){
                proxy=proxy.replace("https://","");
            }
            ss=proxy.split(":");
        }
        System.setProperty("http.proxyHost",ss[0]);
        System.setProperty("http.proxyPort",ss[1]);
        System.setProperty("https.proxyHost",ss[0]);
        System.setProperty("https.proxyPort",ss[1]);
    }
    public static BBookReader getReader() throws BBookException {
        return reader;
    }
    public static String getString(String key) throws BBookException {
        return i18nText.getString(key);
    }

    public static void open(String path) throws BBookException {
        BBook book=null;
        try {
            if(path.toLowerCase().endsWith(".epub")){
                book=getReader().createBookByEpub(path);
            }else {
                book=getReader().createBookByTxt(path,getReader().getCharSet());
            }
        } catch (BBookException e) {
            if(e.msg.equals("epub")){
                throw new BBookException("the epub file format is error");
            }else{
                book=getReader().createBookByTxt(path,getReader().getCharSet());
            }
        }
        book.setCPL(getReader().getCountPerLine());
        getReader().readBook(book);
        book.reset();
        book.saveChapter();
        PP.show(getReader().getReadingBook().currentText());
    }
}
