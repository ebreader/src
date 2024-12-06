package gadget;

import bb.book.BBookException;
import bgzs.BGZS;

public class Base64Button extends GadgetButton{
    @Override
    protected String executeFunction(String inputText) throws BBookException {
        try{
            String algorithm=inputText.substring(0,inputText.indexOf("|"));
            String value=inputText.substring(inputText.indexOf("|")+1);
            if(algorithm.equalsIgnoreCase("d")){
                return HashConvert.decodeB64(value);
            }else if(algorithm.equals("e")){
                return HashConvert.encodeB64(value);
            }
        }catch (Exception e){
        }
        return BGZS.getString("b64tip");
    }
}
