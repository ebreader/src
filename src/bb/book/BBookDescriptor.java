package bb.book;
import bb.simple.ExtensibleBuffer;
import bb.simple.IBuffer;
import bb.simple.SimpleException;
import bb.simple.SimpleFileSave;

import java.io.File;
import java.util.Base64;

public class BBookDescriptor {
    final BBookReader reader;
    final String id;
    String title;
    String chapterRegex;
    int totalCharCount;
    int charCountPerLine;
    int chapterCount;
    int readCharCount;
    int chapterPointer;
    int linePointer;
    int charPointer;
    String recentReadText;
    String recentChapterName;
    boolean valid;


    String bookDir;
    String coolDescriptorFilePath;
    String hotDescriptorFilePath;
    String chapterFilePath;

    public String getTitle() {
        return title;
    }

    private void init(){
        bookDir=reader.getBookStoreDir()+File.separator+id;
        coolDescriptorFilePath= bookDir+File.separator+"descriptor_cool";
        hotDescriptorFilePath= bookDir+File.separator+"descriptor_hot";
        chapterFilePath=bookDir+File.separator+"chapter";
    }
    public String toString(){
        return getPercent()+" "+title +" | "+recentChapterName+" | "+recentReadText;
    }
    public String getPercent(){
        float rc=readCharCount;
        rc=rc/totalCharCount;
        rc=rc*100;
        return "["+String.format("%.2f",rc)+"%]";
    }
    public BBookDescriptor(BBookReader reader,String id) {
        this.reader = reader;
        this.id=id;
        init();
    }

    public void save() throws BBookException {
        saveHot();
        saveCool();
    }

    public void load(){
        try {
            String text=SimpleFileSave.readData(coolDescriptorFilePath);
            decodeCoolDescriptor(text);
            text=SimpleFileSave.readData(hotDescriptorFilePath);
            decodeHotDescriptor(text);
            valid=true;
        } catch (Exception e) {
            title="book invalid";
            valid=false;
        }
    }
    public void saveCool() throws BBookException {
        try {
            SimpleFileSave.writeData(coolDescriptorFilePath,encodeCoolDescriptor());
        } catch (SimpleException e) {
            throw new BBookException(e.msg);
        }
    }
    public void saveHot() throws BBookException {
        try {
            SimpleFileSave.writeData(hotDescriptorFilePath,encodeHotDescriptor());
        } catch (SimpleException e) {
            throw new BBookException(e.msg);
        }
    }

    private String encodeHotDescriptor(){
        IBuffer buffer=new ExtensibleBuffer();
        buffer.putInt(readCharCount);
        buffer.putInt(charPointer);
        buffer.putInt(chapterPointer);
        buffer.putInt(linePointer);
        buffer.putString(recentReadText);
        return Base64.getEncoder().encodeToString(buffer.toArray());
    }
    private void decodeHotDescriptor(String data){
        byte[] bb= Base64.getDecoder().decode(data);
        IBuffer buffer=new ExtensibleBuffer(bb);
        readCharCount=buffer.getInt();
        charPointer=buffer.getInt();
        chapterPointer=buffer.getInt();
        linePointer=buffer.getInt();
        recentReadText=buffer.getString();
    }

    private String encodeCoolDescriptor(){
        IBuffer buffer=new ExtensibleBuffer();
        buffer.putString(title);
        buffer.putString(chapterRegex);
        buffer.putInt(totalCharCount);
        buffer.putInt(charCountPerLine);
        buffer.putInt(chapterCount);
        buffer.putString(recentChapterName);
        return Base64.getEncoder().encodeToString(buffer.toArray());
    }
    private void decodeCoolDescriptor(String data){
        byte[] bb= Base64.getDecoder().decode(data);
        IBuffer buffer=new ExtensibleBuffer(bb);
        title=buffer.getString();
        chapterRegex=buffer.getString();
        totalCharCount=buffer.getInt();
        charCountPerLine=buffer.getInt();
        chapterCount=buffer.getInt();
        recentChapterName=buffer.getString();
    }
}
