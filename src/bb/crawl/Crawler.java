package bb.crawl;
import bb.simple.SimpleCrawler;
import bb.simple.SimpleException;
import bb.simple.SimpleNodeSelector;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class Crawler extends SimpleCrawler {
    private String crawlingURL;
    private String basicURL;
    private String bookName;
    private SimpleNodeSelector chapterSelector;
    private SimpleNodeSelector titleSelector;
    private SimpleNodeSelector contentSelector;
    private boolean singleThread=false;
    public void init(CrawlerDescriptor descriptor) throws CrawlException {
        crawlingURL=descriptor.getCrawlingURL();
        basicURL=descriptor.getBasicURL();
        chapterSelector=new SimpleNodeSelector();
        titleSelector=new SimpleNodeSelector();
        contentSelector=new SimpleNodeSelector();
        chapterSelector.parse(descriptor.getChapterSelector(),null);
        if(descriptor.getTitleSelector().startsWith("@")){
            bookName=descriptor.getTitleSelector().replace("@","");
        }else{
            titleSelector.parse(descriptor.getTitleSelector(),null);
        }
        contentSelector.parse(descriptor.getContentSelector(),null);

        singleThread=descriptor.isSingleThread();
        println("init crawler"+ descriptor.getName());
        println("init crawlingURL"+ descriptor.getCrawlingURL());
        println("init basicURL"+ descriptor.getBasicURL());
        println("init titleSelector "+descriptor.getTitleSelector());
        println("init chapterSelector "+descriptor.getChapterSelector());
        println("init contentSelector "+descriptor.getContentSelector());
        println("singleThread="+singleThread);

    }


    public void setTitleSelector(SimpleNodeSelector titleSelector) {
        this.titleSelector = titleSelector;
    }
    public void setContentSelector(SimpleNodeSelector contentSelector) {
        this.contentSelector = contentSelector;
    }
    public void setCrawlingURL(String crawlingURL) {
        this.crawlingURL = crawlingURL;
    }

    public void setBasicURL(String basicURL) {
        this.basicURL = basicURL;
    }

    public void setChapterSelector(SimpleNodeSelector chapterSelector) {
        this.chapterSelector = chapterSelector;
    }
    public CrawlBook crawling() throws SimpleException {
        Element rootDom=loadURL(crawlingURL);
        println("start parse book title selector ...");
        titleSelector.selectElement(rootDom);
        println("start parse chapter list selector ...");
        chapterSelector.selectElement(rootDom);



        SimpleNodeSelector chapterSelectorNode=chapterSelector.getEndNode();
        String title=bookName;
        if(title==null){
            SimpleNodeSelector titleSelectorNode=titleSelector.getEndNode();
            title=titleSelectorNode.getSpliceFocusData("name");
        }
        println("the book name is "+title);
        CrawlBook book=new CrawlBook(title);
        println("the book("+title+") chapter count="+chapterSelectorNode.getSelectElementList().size()+" ...");
        int ind=0;


        List<ChapterCrawlerThread> threads=new ArrayList<>();
        for(Element chapterElement: chapterSelectorNode.getSelectElementList()){
            String link=chapterSelectorNode.getFocusData("link",chapterElement);
            if(link.isEmpty()){
                link=chapterElement.attr("href");
            }
            String url=createURL(link);
            String name=chapterSelectorNode.getFocusData("name",chapterElement);
            CrawlChapter chapter=new CrawlChapter(name,ind);
            book.addChapter(chapter);
            if(singleThread){
                crawlingChapter(url,book,chapter);
            }else{
                threads.add(new ChapterCrawlerThread(chapter,url,book,this));
            }
            ind++;
        }
        if(!singleThread){
            for(ChapterCrawlerThread cc:threads){
                cc.start();
            }

            for(ChapterCrawlerThread cc:threads){
                cc.join();
            }
        }
        return book;
    }


    void crawlingChapter(String url,CrawlBook book,CrawlChapter chapter) throws SimpleException {
        println("the chapter ("+chapter.getName()+") link="+url);
        Element contentRootElement=loadURL(url);
        contentSelector.resetSelectElement();
        contentSelector.selectElement(contentRootElement);
        SimpleNodeSelector contentSelectorNode=contentSelector.getEndNode();
        for(Element contentElement:contentSelectorNode.getSelectElementList()){
            chapter.addContent(contentSelectorNode.getFocusData("content",contentElement));
        }
        println("crawling book("+book.getTitle()+") chapter("+chapter.getName()+")."+chapter.getIndex()+" success");
    }

    public String createURL(String sub){
        if(basicURL==null||basicURL.isEmpty()){
            return sub;

        }
        if(basicURL.contains("{#}")){
            return basicURL.replace("{#}",sub);
        }else {
            return basicURL+"/"+sub;
        }

    }
}
