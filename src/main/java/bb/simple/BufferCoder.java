package bb.simple;

import java.nio.ByteBuffer;
public class BufferCoder {
    public static byte decodeByte(byte[] data,int pos){
        return data[pos];
    }
    public static short decodeShort(byte[] data,int pos){
        return ByteBuffer.wrap(data,pos,2).getShort();
    }
    public static int decodeInt(byte[] data,int pos){
        return ByteBuffer.wrap(data,pos,4).getInt();
    }
    public static long decodeLong(byte[] data,int pos){
        return ByteBuffer.wrap(data,pos,8).getLong();
    }
    public static float decodeFloat(byte[] data,int pos){
        return ByteBuffer.wrap(data,pos,4).getFloat();
    }
    public static double decodeDouble(byte[] data,int pos){
        return ByteBuffer.wrap(data,pos,8).getDouble();
    }
    public static boolean decodeBool(byte[] data,int pos){
        return data[pos]==1;
    }


    public static void encodeByte(byte[] data,int pos,int value){
        data[pos]= (byte) value;
    }

    public static void encodeShort(byte[] data,int pos,int value){
        ByteBuffer.wrap(data,pos,2).putShort((short) value);
    }
    public static void encodeInt(byte[] data,int pos,int value){
        ByteBuffer.wrap(data,pos,4).putInt(value);
    }
    public static void encodeLong(byte[] data,int pos,long value){
        ByteBuffer.wrap(data,pos,8).putLong(value);
    }
    public static void encodeFloat(byte[] data,int pos,double value){
        ByteBuffer.wrap(data,pos,4).putFloat((float) value);
    }
    public static void  encodeDouble(byte[] data,int pos,double value){
        ByteBuffer.wrap(data,pos,8).putDouble(value);
    }
    public static void  encodeBool(byte[] data,int pos,boolean value) {
        if (value) {
            data[pos] = 1;
        }else{
            data[pos]=0;
        }
    }
}
