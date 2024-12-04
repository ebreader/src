package bb.book;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BChapter {
    int index;
    String name;
    String realName;
    BBook book;
    final List<String> originContent;

    public BChapter(int index, String name, BBook book, List<String> originContent) {
        this.index = index;
        this.realName=name;
        this.name = (index+1)+"."+name;
        this.book = book;
        this.originContent = originContent;
    }
    public String toText(){
        StringBuilder stb=new StringBuilder();
        stb.append(BBSymbol.CHAPTER_PREFIX).append(realName);
        for(String st:originContent){
            stb.append(BBSymbol.BR);
            stb.append(BBSymbol.CONTENT_PREFIX).append(st);
        }
        return stb.toString();
    }
    public void setIndex(int index){
        this.name=this.name.replace(this.index+".","");
        this.index=index;
        this.name=index+"."+this.name;
    }
    public String getRealName(){
        return realName;
    }


    public void fixName(Pattern pattern){
        for(String s:originContent){
            Matcher matcher = pattern.matcher(s);
            if(matcher.find()){
                this.name=index+"."+s;
                return;
            }
        }
    }

    public String getLine(int linePointer){
        return originContent.get(linePointer);
    }
    public int getLastLinePointer(){
        return originContent.size()-1;
    }
    public String toString(){
        return name;
    }
    public void locate() throws BBookException {
        book.locateChapter(this);
    }
    public int chapterCharCount(){
        int c=0;
        for(String s:originContent){
            c+=s.length();
        }
        return c;
    }
}
