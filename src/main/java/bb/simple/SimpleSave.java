package bb.simple;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SimpleSave {
    private final Map<String,String> map=new HashMap<>();
    public void set(String key,String value){
        map.put(key,value);
    }
    public String get(String key){
        return map.get(key);
    }
    public void load(String path) throws SimpleException {
        String data= SimpleFileSave.readData(path);
        decode(data);
    }
    private void decode(String data){
        if(data==null|| data.isEmpty()){
            return;
        }
        map.clear();
        byte[] bb=Base64.getDecoder().decode(data);
        IBuffer buffer=new ExtensibleBuffer(bb);
        int size=buffer.getInt();
        for(int i=0;i<size;i++){
            map.put(buffer.getString(),buffer.getString());
        }
    }
    private String encode(){
        IBuffer buffer=new ExtensibleBuffer();
        buffer.putInt(map.size());
        for(Map.Entry<String,String> ss:map.entrySet()){
            buffer.putString(ss.getKey());
            buffer.putString(ss.getValue());
        }
        return Base64.getEncoder().encodeToString(buffer.toArray());
    }
    public void save(String path) throws SimpleException {
        SimpleFileSave.writeData(path,encode());
    }
}
