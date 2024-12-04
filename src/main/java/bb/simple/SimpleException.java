package bb.simple;

public class SimpleException extends Exception{
    public final String msg;

    public SimpleException(String message) {
        super(message);
        this.msg = message;
    }
}
