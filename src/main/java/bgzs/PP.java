package bgzs;

import bb.simple.ISimpleOut;
public class PP {
    private final static ISimpleOut out=new PluginOut();
    public static void show(String msg) {
        out.println(msg);
    }

    public static void clean(){
        out.clean();
    }

    public static ISimpleOut getOut(){
        return out;
    }



}
