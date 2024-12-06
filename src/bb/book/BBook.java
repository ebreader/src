package bb.book;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BBook {
    public static final String CHAPTER_MASK="###chapter###.";
    final BBookDescriptor descriptor;
    private final List<BChapter> chapters=new ArrayList<>();
    public BBook(BBookDescriptor descriptor) {
        this.descriptor=descriptor;
        setCPL(descriptor.reader.getCountPerLine());
    }
    public void lastChapter() throws BBookException {
        int ii=descriptor.chapterPointer-1;
        if(ii<0){
            ii=chapters.size()-1;
        }
        if(ii<0){
            return;
        }
        BChapter cc=chapters.get(ii);
        cc.locate();
    }

    public void nextChapter() throws BBookException {
        int ii=descriptor.chapterPointer+1;
        if(ii>=chapters.size()){
            ii=0;
        }
        if(ii>=chapters.size()){
            return;

        }
        BChapter cc=chapters.get(ii);
        cc.locate();
    }


    public String toText(){
        StringBuilder stb=new StringBuilder();
        stb.append(BBSymbol.MAGIC).append(BBSymbol.BR);
        stb.append(BBSymbol.TITLE_PREFIX).append(descriptor.title);
        for(BChapter chapter:chapters){
            stb.append(BBSymbol.BR);
            stb.append(chapter.toText());
        }
        return stb.toString();
    }


    public void setTitle(String v){
        descriptor.title=v;
    }
    public String getChapterRegex(){
        return descriptor.chapterRegex;
    }

    public String getTitle(){
        return descriptor.title;
    }

    public BBookDescriptor getDescriptor() {
        return descriptor;
    }

    public void save() throws BBookException {
        descriptor.save();
        saveChapter();
    }
    public List<String> getBookContent(){
        List<String> cc=new ArrayList<>();
        for(BChapter ch:chapters){
            cc.addAll(ch.originContent);
        }
        return cc;
    }
    public boolean setChapterRegex(String regex) throws BBookException {
        descriptor.chapterRegex=regex;
        descriptor.saveCool();
        return indexChapterWithRegex();
    }



    public void resetChapterName() throws BBookException {
        if(descriptor.chapterRegex==null||descriptor.chapterRegex.isEmpty()){
            return;
        }
        Pattern pattern = Pattern.compile(descriptor.chapterRegex);
        for(int i=0;i<chapters.size();i++){
            BChapter chapter=chapters.get(i);
            chapter.fixName(pattern);
            if(i==0&&chapter.name.startsWith("1."+CHAPTER_MASK)){
                chapter.name=descriptor.reader.getString("preface");
            }
        }
    }

    public boolean indexChapterWithRegex() throws BBookException {
        if(descriptor.chapterRegex==null||descriptor.chapterRegex.isEmpty()){
            descriptor.chapterRegex= descriptor.reader.getString("defaultChapterRegex");
        }

        List<BChapter> newList=new ArrayList<>();
        Pattern pattern = Pattern.compile(descriptor.chapterRegex);
        BChapter chapter=null;
        BChapter xuzhang=null;
        List<String> contentList=getBookContent();
        boolean zhaodao=false;
        for(String s:contentList){
            Matcher matcher = pattern.matcher(s);
            if(matcher.find()){
                zhaodao=true;
                chapter=new BChapter(newList.size(),s,this,new ArrayList<>());
                newList.add(chapter);
                chapter.originContent.add(s);
            }else if(chapter!=null){
                chapter.originContent.add(s);
            }else{
                if(xuzhang==null){
                    xuzhang=new BChapter(newList.size(),descriptor.reader.getString("preface"),this,new ArrayList<>());
                    newList.add(xuzhang);
                }
                xuzhang.originContent.add(s);
            }
        }


        descriptor.totalCharCount=0;
        for(BChapter cp:chapters){
            descriptor.totalCharCount+=cp.chapterCharCount();
        }

        if(zhaodao){
            chapters.clear();
            chapters.addAll(newList);
            reset();
            saveChapter();
        }

        return zhaodao;

    }
    public void reset() throws BBookException {
        descriptor.readCharCount=0;
        descriptor.chapterPointer=0;
        descriptor.linePointer=0;
        descriptor.charPointer=0;
        descriptor.recentReadText=getCurrentText();
        descriptor.readCharCount=descriptor.recentReadText.length();
        descriptor.recentChapterName=chapters.get(descriptor.chapterPointer).name;
        descriptor.save();
    }

    public String getID(){
        return descriptor.id;
    }
    void locateChapter(BChapter chapter) throws BBookException {
        int cc=0;
        for(int i=0;i<chapters.size();i++){
            if(i<chapter.index){
                BChapter ccc=chapters.get(i);
                cc+=ccc.chapterCharCount();
            }else{
                break;
            }
        }


        descriptor.readCharCount=cc;
        descriptor.chapterPointer=chapter.index;
        descriptor.linePointer=0;
        descriptor.charPointer=0;
        descriptor.recentChapterName=chapter.name;
        descriptor.recentReadText=getCurrentText();
        descriptor.readCharCount+=descriptor.recentReadText.length();
        descriptor.saveHot();
        descriptor.saveCool();

    }
    public void loadChapter() throws BBookException{
        File file=new File(descriptor.chapterFilePath);
        if(!file.exists()){
            throw new BBookException("the book is invalid please reopen this book");
        }
        BufferedReader br=null;
        try{
            br=new BufferedReader(new InputStreamReader(new FileInputStream(descriptor.chapterFilePath), StandardCharsets.UTF_8));
            String s=null;
            BChapter curChapter=null;
            while((s=br.readLine())!=null){
                if(s.isEmpty()){
                    continue;
                }
                if(s.startsWith(CHAPTER_MASK)){
                    curChapter=new BChapter(chapters.size(),s.replace(CHAPTER_MASK,""),this,new ArrayList<>());
                    chapters.add(curChapter);
                }else{
                    if(curChapter!=null){
                        curChapter.originContent.add(s);
                    }
                }
            }
        }catch (Exception e){
            throw new BBookException("save chapter error");
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    throw new BBookException("save chapter close buffered writer error");
                }
            }

        }
    }

    public int getChapterPointer(){
        return descriptor.chapterPointer;
    }
    public List<BChapter> getChapters() {
        return chapters;
    }

    public void saveChapter() throws BBookException {
        File file=new File(descriptor.chapterFilePath);
        if(file.exists()){
            if(!file.delete()){
                throw new BBookException("save chapter delete old file error");
            }
            try {
                if(!file.createNewFile()){
                    throw new BBookException("save chapter create file error");
                }
            } catch (IOException e) {
                throw new BBookException("save chapter create file error");
            }
        }
        BufferedWriter br=null;
        try{
            br=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(descriptor.chapterFilePath), StandardCharsets.UTF_8));
            int i=0;
            for(BChapter bc:chapters){
                if(i>0){
                    br.newLine();
                }
                i++;
                br.write(CHAPTER_MASK+bc.getRealName());
                for(String s:bc.originContent){
                    br.newLine();
                    br.write(s);
                }
            }
            br.flush();
        }catch (Exception e){
            throw new BBookException("save chapter error");
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    throw new BBookException("save chapter close buffered writer error");
                }
            }

        }

    }

    private void nextCharPointer(){
        int endIndex=descriptor.charPointer+descriptor.charCountPerLine;
        BChapter chapter=chapters.get(descriptor.chapterPointer);
        String line=chapter.getLine(descriptor.linePointer);
        int totalLength=line.length();
        if(endIndex>totalLength){
            endIndex=totalLength;
        }
        if(endIndex==totalLength){
            nextLinePointer();
        }else{
            descriptor.charPointer=endIndex;
        }
    }
    private void nextLinePointer(){
        BChapter chapter=chapters.get(descriptor.chapterPointer);
        if(descriptor.linePointer==chapter.getLastLinePointer()){
            nextChapterPointer();
        }else{
            descriptor.linePointer++;
            descriptor.charPointer=0;
        }
    }
    private void nextChapterPointer(){
        if(descriptor.chapterPointer==chapters.size()-1){
            return;
        }
        descriptor.chapterPointer++;
        descriptor.linePointer=0;
        descriptor.charPointer=0;
    }

    private void lastCharPointer(){
        if(descriptor.charPointer<=0){
            lastLinePointer();
        }else{
            descriptor.charPointer=descriptor.charPointer-descriptor.charCountPerLine;
            if(descriptor.charPointer<0){
                descriptor.charPointer=0;
            }
        }
    }
    private void lastLinePointer(){
        if(descriptor.linePointer<=0){
            lastChapterPointer();
        }else{
            descriptor.linePointer--;
            BChapter chapter=chapters.get(descriptor.chapterPointer);
            String line=chapter.getLine(descriptor.linePointer);
            descriptor.charPointer=line.length()-descriptor.charCountPerLine;
            if(descriptor.charPointer<0){
                descriptor.charPointer=0;
            }
        }
    }
    private void lastChapterPointer(){
        if(descriptor.chapterPointer<=0){
            descriptor.chapterPointer=0;
        }else{
            descriptor.chapterPointer--;
            BChapter chapter=chapters.get(descriptor.chapterPointer);
            descriptor.linePointer=chapter.getLastLinePointer();
            String line=chapter.getLine(descriptor.linePointer);
            descriptor.charPointer=line.length()-descriptor.charCountPerLine;
            if(descriptor.charPointer<0){
                descriptor.charPointer=0;
            }
        }
    }

    public String lastText() throws BBookException {
        int cp=descriptor.chapterPointer;
        int lp=descriptor.linePointer;
        int ccp=descriptor.charPointer;
        lastCharPointer();
        String st;
        if(cp==descriptor.chapterPointer&&lp==descriptor.linePointer&&ccp==descriptor.charPointer){
            st=getCurrentText();
        }else{
            st=getCurrentText();
            if(descriptor.chapterPointer!=cp){
                descriptor.recentChapterName=chapters.get(descriptor.chapterPointer).name;
                descriptor.saveCool();
            }
            descriptor.readCharCount-=st.length();
            if(descriptor.readCharCount<=0){
                descriptor.readCharCount=st.length();
            }
            descriptor.recentReadText=st;
            descriptor.saveHot();
        }
        return currentText();
    }

    public String nextText() throws BBookException {
        int cp=descriptor.chapterPointer;
        int lp=descriptor.linePointer;
        int ccp=descriptor.charPointer;
        nextCharPointer();
        String st;
        if(cp==descriptor.chapterPointer&&lp==descriptor.linePointer&&ccp==descriptor.charPointer){
            st=getCurrentText();
        }else{
            st=getCurrentText();
            if(descriptor.chapterPointer!=cp){
                descriptor.recentChapterName=chapters.get(descriptor.chapterPointer).name;
                descriptor.saveCool();
            }
            descriptor.readCharCount+=st.length();
            if(descriptor.readCharCount>descriptor.totalCharCount){
                descriptor.readCharCount=descriptor.totalCharCount;
            }
            descriptor.recentReadText=st;
            descriptor.saveHot();
        }
        return currentText();
    }

    public String currentText(){
        BChapter chapter=chapters.get(descriptor.chapterPointer);
        return descriptor.getPercent()+"["+chapter.getRealName()+"]  "+getCurrentText();
    }

    private String getCurrentText(){
        BChapter chapter=chapters.get(descriptor.chapterPointer);
        String line=chapter.getLine(descriptor.linePointer);
        int totalLength=line.length();
        int endIndex=descriptor.charPointer+descriptor.charCountPerLine;
        if(endIndex>totalLength){
            endIndex=totalLength;
        }
        return line.substring(descriptor.charPointer,endIndex);
    }

    public void setCPL(int cpl){
        descriptor.charCountPerLine=cpl;
    }
    public void createChapter(List<String> chapterContent,String chapterTitle){
        if(chapterContent.isEmpty()){
            return;
        }
        BChapter chapter=new BChapter(chapters.size(),chapterTitle,this,chapterContent);
        int charCount=0;
        for(String ss:chapterContent){
            charCount+=ss.length();
        }
        descriptor.totalCharCount+=charCount;
        chapters.add(chapter);
        descriptor.chapterCount++;
    }
}
