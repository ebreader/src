package bb.crawl;

import bb.book.BBSymbol;

import java.util.ArrayList;
import java.util.List;

public class CrawlChapter {
    private final String name;
    private int index;
    private final List<String> contentList=new ArrayList<>();

    public String toText(){
        StringBuilder stb=new StringBuilder();
        stb.append(BBSymbol.CHAPTER_PREFIX).append(name);
        for(String st:contentList){
            stb.append(BBSymbol.BR);
            stb.append(BBSymbol.CONTENT_PREFIX).append(st);
        }
        return stb.toString();
    }
    public CrawlChapter(String name,int index) {
        this.index=index;
        this.name = name;
    }
    public void addContent(String p){
        if(p==null||p.isEmpty()){
            return;
        }
        contentList.add(p);
    }
    public boolean isEmpty(){
        return contentList.isEmpty();
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public List<String> getContentList() {
        return contentList;
    }
}
