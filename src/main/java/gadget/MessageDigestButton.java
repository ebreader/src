package gadget;

import bb.book.BBookException;
import bgzs.BGZS;

public class MessageDigestButton extends GadgetButton{
    @Override
    protected String executeFunction(String inputText) throws BBookException {
        try{
            String algorithm=inputText.substring(0,inputText.indexOf("|"));
            String value=inputText.substring(inputText.indexOf("|")+1);
            return HashConvert.computeHash(value,algorithm);
        }catch (Exception e){
            return BGZS.getString("nohas");
        }
    }
}
