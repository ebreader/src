package bb.book;
import bb.simple.*;
import bgzs.BGZS;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BBookReader {
    private final String workDir;
    private BBook readingBook;
    private I18nText i18n;
    private static final SimpleSave setting=new SimpleSave();
    public BBookReader(String workDir) throws BBookException {
         this.workDir = workDir;
        try {
            setting.load(getSettingFile());
        } catch (SimpleException e) {
            throw new BBookException(e.msg);
        }
    }
    public String get(String key){
        return setting.get(key);
    }

    public void set(String key,String value){
        setting.set(key,value);

    }

    public void setI18n(I18nText i18n) {
        this.i18n = i18n;
    }
    public void setSearchEngine(String url,boolean save) throws BBookException {
        if(url==null||url.isEmpty()){
            url="https://www.baidu.com";
        }
        setting.set("se",url);
        if(save){
            saveSetting();
        }
    }
    public String getSearchEngine(){
        String url=setting.get("se");
        if(url==null||url.isEmpty()){
            url="https://www.baidu.com";
        }
        return url;
    }


    public void setProxy(String url,boolean save) throws BBookException {
        if(url==null||url.isEmpty()){
            url="";
        }
        setting.set("proxy",url);
        if(save){
            saveSetting();
        }
    }
    public String getProxy(){
        String mm=setting.get("proxy");
        if(mm==null){
            mm="";
        }
        return mm;
    }

    public void setAI(String url,boolean save) throws BBookException {
        if(url==null||url.isEmpty()){
            url="https://tongyi.aliyun.com";
        }
        setting.set("ai",url);
        if(save){
            saveSetting();
        }
    }
    public String getAI(){
        String url=setting.get("ai");
        if(url==null||url.isEmpty()){
            url="https://tongyi.aliyun.com";
        }
        return url;
    }

    public void setTransfer(String url,boolean save) throws BBookException {
        if(url==null||url.isEmpty()){
            url="https://www.youdao.com";
        }
        setting.set("tr",url);
        if(save){
            saveSetting();
        }
    }
    public String getTransfer(){
        String url=setting.get("tr");
        if(url==null||url.isEmpty()){
            url="https://www.youdao.com";
        }
        return url;
    }

    public String getString(String key){
        if(i18n==null){
            return key;
        }
        return i18n.getString(key);

    }
    public String getLanguage(){
        return setting.get("language");
    }
    public void setLanguage(String language,boolean save) throws BBookException {
        setting.set("language",language);
        if(save){
            saveSetting();
        }
    }

    public String getWorkDir() {
        return workDir;
    }

    public String getBookStoreDir(){
        return getWorkDir()+ File.separator+"books";
    }
    public String getSettingFile(){
        return getWorkDir()+File.separator+"setting";
    }
    public List<BBookDescriptor> getBookDescriptorList(){
        List<BBookDescriptor> list=new ArrayList<>();
        File file=new File(getBookStoreDir());
        if(!file.exists()){
            return list;
        }

        File[] files=file.listFiles();
        if(files==null||files.length==0){
            return list;
        }


        for(int i=0;i<files.length;i++){
            File f=files[i];
            if(f.isFile()){
                continue;
            }
            BBookDescriptor descriptor=new BBookDescriptor(this,f.getName());
            descriptor.load();
            list.add(descriptor);
        }
        String id=setting.get("cb");
        if(id==null||id.isEmpty()){
            return list;
        }

        BBookDescriptor descriptor=null;
        for(int i=0;i<list.size();i++){
            BBookDescriptor bd=list.get(i);
            if(bd.id.equals(id)){
                list.remove(i);
                descriptor=bd;
                break;
            }
        }
        if(descriptor!=null){
            list.add(0,descriptor);
        }

        return list;
    }

    public String generateID(String identity){
        return StringTool.md5(identity);
    }

    public String getCharSet(){
        String s=setting.get("char");
        if(s==null|| s.isEmpty()){
            return "UTF-8";
        }
        return s;
    }

    public void setCharSet(String cha,boolean save) throws BBookException {
        if(cha==null||cha.isEmpty()){
            return;
        }
        setting.set("char",cha);
        if(save){
            saveSetting();
        }
    }
    public void setCountPerLine(int cpl,boolean save) throws BBookException {
        if(cpl<=0){
            return;
        }
        setting.set("cpl",String.valueOf(cpl));
        if(save){
            saveSetting();
        }
        if(readingBook!=null){
            readingBook.setCPL(cpl);
        }
    }
    public void saveSetting() throws BBookException {
        try {
            setting.save(getSettingFile());
        } catch (SimpleException e) {
            throw new BBookException(e.msg);
        }
    }
    public int getCountPerLine(){
        String cpl=setting.get("cpl");
        if(cpl==null){
            return 100;
        }

        try{
            int c=Integer.parseInt(cpl);
            if(c==0){
                return 100;
            }
            return c;
        }catch (Exception e){
            return 100;

        }
    }
    private BBook createBookByMagic(String filePath,List<String> contentList) throws BBookException {
        File f=new File(filePath);
        BBook bb=new BBook(new BBookDescriptor(this,generateID(filePath)));
        List<String> chapterList=null;
        String chapterTag=null;
        for(int i=1;i<contentList.size();i++){
            String st=contentList.get(i);
            if(st.startsWith(BBSymbol.TITLE_PREFIX)){
                bb.setTitle(st.replace(BBSymbol.TITLE_PREFIX,""));
            }else if(st.startsWith(BBSymbol.CHAPTER_PREFIX)){
                if(chapterList != null){
                    bb.createChapter(chapterList,chapterTag);
                }
                chapterTag=st.replace(BBSymbol.CHAPTER_PREFIX,"");
                chapterList=new ArrayList<>();
            }else if(st.startsWith(BBSymbol.CONTENT_PREFIX)){
                if(chapterList!=null){
                    chapterList.add(st.replace(BBSymbol.CONTENT_PREFIX,""));
                }
            }
        }
        bb.save();
        return bb;
    }
    public BBook createBookByTxt(String filePath,String cs) throws BBookException {
        List<String> list=new ArrayList<>();
        BufferedReader br=null;
        try {
            br=new BufferedReader(new InputStreamReader(new FileInputStream(filePath),cs));
            String st=null;
            while((st=br.readLine())!=null){
                if(st.trim().equals("")){
                    continue;
                }
                list.add(st);
            }

        } catch (Exception e) {
            throw new BBookException("create stream error "+e.getMessage());
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    throw new BBookException("close stream error "+filePath);
                }
            }
        }



        if(!list.isEmpty()){
            String magic=list.get(0).trim();
            if(BBSymbol.MAGIC.equals(magic)){
                return createBookByMagic(filePath,list);
            }
        }



        String chapterTag="Init";
        if(!list.isEmpty()){
            chapterTag=chapterTag+"#"+list.get(0);
        }

        File f=new File(filePath);
        BBook bb=new BBook(new BBookDescriptor(this,generateID(filePath)));
        bb.setTitle(f.getName());
        bb.createChapter(list,chapterTag);


        bb.setChapterRegex(getString("defaultChapterRegex"));
        return bb;
    }


    public String detectEncoding(byte[] bd){
        String uu= null;
        try {
            uu = StringTool.detectEncoding(bd);
        } catch (SimpleException e) {
        }
        if(uu==null){
            uu=getCharSet();
        }
        return uu;

    }






    public BBook createBookByEpub(String path) throws BBookException {

        BBook bb=new BBook(new BBookDescriptor(this,generateID(path)));
        try{
            File epubFile = new File(path);
            FileInputStream epubFileStream = new FileInputStream(epubFile);

            EpubReader epubReader = new EpubReader();
            Book book = epubReader.readEpub(epubFileStream);
            String title=book.getTitle();
            if(title==null||title.equals("")){
                title=epubFile.getName();
            }
            bb.setTitle(title);
            Spine spine=book.getSpine();
            if(spine==null){
                throw new BBookException("epub");
            }
            List<SpineReference> refList=spine.getSpineReferences();
            if(spine.getSpineReferences().isEmpty()){
                throw new BBookException("epub");
            }

            int index=0;
            for(SpineReference srf:refList){
                byte[] bd=srf.getResource().getData();
                String content=new String(bd,detectEncoding(bd));
                Document document= Jsoup.parse(content);
                List<String> sl=new ArrayList<>();
                for(Element e:document.getAllElements()){
                    String ot=e.ownText();
                    if(ot.equals("")){
                        continue;
                    }
                    sl.add(ot);
                }
                if(sl.size()>0){
                    String chapterTag=sl.get(0);
                    bb.createChapter(sl,chapterTag);
                    index++;
                }
            }
            bb.setChapterRegex(getString("defaultChapterRegex"));
        }catch (BBookException e){
            throw e;
        }catch (Exception e){
            throw new BBookException("read epub "+path+" error");
        }
        return bb;
    }

    private void fillEPubChapter(String superChapterTag,BBook bb,TOCReference tocReference,Set<Resource> ret) throws BBookException {
        try{
            String  chapterTag=null;
            if(superChapterTag==null){
                chapterTag=tocReference.getTitle();
            }else{
                chapterTag=superChapterTag+"#"+tocReference.getTitle();
            }
            if(tocReference.getChildren().size()>0){
                for(TOCReference toc: tocReference.getChildren()){
                    fillEPubChapter(chapterTag,bb,toc,ret);
                }
            }else{
                if(ret.contains(tocReference.getResource())){
                    return;
                }
                byte[] bd=tocReference.getResource().getData();
                String content=new String(bd,detectEncoding(bd));
                Document document= Jsoup.parse(content);
                List<String> sl=new ArrayList<>();
                for(Element e:document.getAllElements()){
                    String ot=e.ownText();
                    if(ot==null||ot.equals("")){
                        continue;
                    }
                    sl.add(ot);
                }
                bb.createChapter(sl,chapterTag);
                ret.add(tocReference.getResource());
            }
        }catch (Exception e){
            throw new BBookException("fillEPubChapter error");
        }
    }

    public void continueReadBook(BBookDescriptor descriptor) throws BBookException {
        BBook book=new BBook(descriptor);
        book.loadChapter();
        book.resetChapterName();
        readBook(book);
    }

    public void removeBook(BBookDescriptor descriptor) throws BBookException {
        try {
            SimpleFileSave.deleteDir(descriptor.bookDir);
            if(readingBook!=null){
                if(descriptor.id.equals(readingBook.getID())){
                    readingBook=null;
                }
            }
        } catch (SimpleException e) {
            throw new BBookException(e.msg);
        }

    }
    public void readBook(BBook book) throws BBookException {
        readingBook=book;
        setting.set("cb",book.getID());
        try {
            setting.save(getSettingFile());
        } catch (SimpleException e) {
            throw new BBookException(e.msg);
        }
    }

    public BBook getReadingBook() throws BBookException {
        if(readingBook==null){
            List<BBookDescriptor> list= BGZS.getReader().getBookDescriptorList();
            if(!list.isEmpty()){
                continueReadBook(list.get(0));
            }
        }
        return readingBook;
    }
}
