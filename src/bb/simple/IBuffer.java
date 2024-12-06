package bb.simple;

public interface IBuffer {
    byte getByte();
    short getShort();
    int getInt();
    long getLong();
    float getFloat();
    boolean getBool();
    double getDouble();
    String getString();
    String getText();
    byte[] getBlob();

    void putBool(boolean v);
    void putByte(int v);
    void putShort(int v);
    void putInt(int v);
    void putLong(long v);
    void putFloat(float v);
    void putDouble(double v);
    void putString(String v);
    void putText(String v);
    void putBlob(byte[] bb);
    byte[] toArray();
}
