package bb.simple;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;

public class SimpleCrawler {

    private ISimpleOut out;
    public void setOut(ISimpleOut out) {
        this.out = out;
    }
    public void println(String t){
        if(out!=null){
            out.println(t);
        }
    }
    public void cleanOut(){
        out.clean();
    }
    public Element loadURL(String url) throws SimpleException{
        Exception eem=null;
        for(int i=0;i<60;i++){
            println("begin crawling url="+url);
            Element rootDom;
            try {
                Document document= Jsoup.parse(new URI(url).toURL(),60000);
                rootDom=document.root();
            }catch (Exception e){
                eem=e;
                println("load url error "+e.getMessage()+" wait 2 second and retry ...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    eem=ex;
                }
                continue;
            }
            println("crawling url="+url+" success");
            return rootDom;
        }
        throw new SimpleException("load uri error " + StringTool.exceptionStack(eem));

    }
}
