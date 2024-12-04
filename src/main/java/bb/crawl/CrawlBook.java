package bb.crawl;

import bb.book.BBSymbol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CrawlBook {
    private final String title;
    private final List<CrawlChapter> chapterList=new ArrayList<>();

    public CrawlBook(String title) {
        this.title = title;
    }

    public String toText(){
        StringBuilder stb=new StringBuilder();
        stb.append(BBSymbol.MAGIC).append(BBSymbol.BR);
        stb.append(BBSymbol.TITLE_PREFIX).append(title);
        for(CrawlChapter cc:chapterList){
            stb.append(BBSymbol.BR);
            stb.append(cc.toText());
        }
        return stb.toString();
    }
    public void addChapter(CrawlChapter chapter){
        chapterList.add(chapter);
    }

    public String getTitle() {
        return title;
    }

    public List<CrawlChapter> getChapterList() {
        chapterList.sort(Comparator.comparingInt(CrawlChapter::getIndex));
        return chapterList;
    }
}
