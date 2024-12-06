package bb.simple;
import java.nio.charset.StandardCharsets;
public class ExtensibleBuffer implements IBuffer{
    private byte[] data;
    private int getPos;
    private int putPos;
    private final int capacity;

    public ExtensibleBuffer() {
        data=new byte[256];
        this.capacity=data.length;
    }

    public ExtensibleBuffer(byte[] data) {
        this.data = data;
        this.capacity=data.length;
    }

    public ExtensibleBuffer(int capacity) {
        this.data = new byte[capacity];
        this.capacity=data.length;
    }
    private void extend(int cc){
        if(data==null){
            data=new byte[cc];
        }else{
            byte[] d=new byte[data.length+cc];
            System.arraycopy(data,0,d,0,data.length);
            data=d;
        }
    }
    private void checkExtend(int pos){
        if(pos>=data.length){
            int que=pos-data.length+1;
            extend(capacity+que);
        }
    }


    @Override
    public byte getByte() {
        return data[getPos++];
    }

    @Override
    public short getShort() {
        short v=BufferCoder.decodeShort(data,getPos);
        getPos+=2;
        return v;
    }

    @Override
    public int getInt() {
        int v=BufferCoder.decodeInt(data,getPos);
        getPos+=4;
        return v;
    }

    @Override
    public long getLong() {
        long v=BufferCoder.decodeLong(data,getPos);
        getPos+=8;
        return v;
    }

    @Override
    public float getFloat() {
        float v=BufferCoder.decodeFloat(data,getPos);
        getPos+=4;
        return v;
    }

    @Override
    public boolean getBool() {
        boolean v=BufferCoder.decodeBool(data,getPos);
        getPos++;
        return v;
    }

    @Override
    public double getDouble() {
        double v=BufferCoder.decodeDouble(data,getPos);
        getPos+=8;
        return v;
    }

    @Override
    public String getString() {
        int ss=getShort();
        if(ss==0){
            return "";
        }
        String v=new String(data,getPos,ss, StandardCharsets.UTF_8);
        getPos+=ss;
        return v;
    }

    @Override
    public String getText() {
        int ss=getInt();
        if(ss==0){
            return "";
        }
        String v=new String(data,getPos,ss, StandardCharsets.UTF_8);
        getPos+=ss;
        return v;
    }

    @Override
    public byte[] getBlob() {
        int ss=getInt();
        byte[] bb=new byte[ss];
        if(bb.length==0){
            return bb;
        }
        System.arraycopy(data,getPos,bb,0,ss);
        getPos+=ss;
        return bb;
    }
    @Override
    public void putBool(boolean v) {
        checkExtend(putPos+1);
        BufferCoder.encodeBool(data,putPos,v);
        putPos++;
    }

    @Override
    public void putByte(int v) {
        checkExtend(putPos+1);
        data[putPos]= (byte) v;
        putPos++;

    }

    @Override
    public void putShort(int v) {
        checkExtend(putPos+2);
        BufferCoder.encodeShort(data,putPos,v);
        putPos+=2;
    }

    @Override
    public void putInt(int v) {
        checkExtend(putPos+4);
        BufferCoder.encodeInt(data,putPos,v);
        putPos+=4;
    }

    @Override
    public void putLong(long v) {
        checkExtend(putPos+8);
        BufferCoder.encodeLong(data,putPos,v);
        putPos+=8;
    }

    @Override
    public void putFloat(float v) {

        checkExtend(putPos+4);
        BufferCoder.encodeFloat(data,putPos,v);
        putPos+=4;

    }

    @Override
    public void putDouble(double v) {
        checkExtend(putPos+8);
        BufferCoder.encodeDouble(data,putPos,v);
        putPos+=8;
    }

    @Override
    public void putString(String v) {
        if(v==null|| v.isEmpty()){
            putShort(0);
            return;
        }


        byte[] bm=v.getBytes(StandardCharsets.UTF_8);
        putShort(bm.length);
        checkExtend(putPos+bm.length);
        System.arraycopy(bm,0,data,putPos,bm.length);
        putPos+=bm.length;
    }

    @Override
    public void putText(String v) {

        if(v==null|| v.isEmpty()){
            putInt(0);
            return;
        }
        byte[] bm=v.getBytes(StandardCharsets.UTF_8);
        putInt(bm.length);
        checkExtend(putPos+bm.length);
        System.arraycopy(bm,0,data,putPos,bm.length);
        putPos+=bm.length;
    }

    @Override
    public void putBlob(byte[] bb) {

        if(bb==null||bb.length==0){
            putInt(0);
            return;
        }

        putInt(bb.length);
        checkExtend(putPos+bb.length);
        System.arraycopy(bb,0,data,putPos,bb.length);
        putPos+=bb.length;
    }
    @Override
    public byte[] toArray() {
        byte[] bb=new byte[putPos+1];
        System.arraycopy(data,0,bb,0,bb.length);
        return bb;
    }
}
