package gadget;

import bb.book.BBookException;
import bgzs.BGZS;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLButton extends GadgetButton{
    @Override
    protected String executeFunction(String inputText) throws BBookException {
        try{
            String algorithm=inputText.substring(0,inputText.indexOf("|"));
            String value=inputText.substring(inputText.indexOf("|")+1);
            if(algorithm.equalsIgnoreCase("d")){
                return URLDecoder.decode(value,StandardCharsets.UTF_8);
            }else if(algorithm.equals("e")){
                return URLEncoder.encode(value, StandardCharsets.UTF_8);
            }
        }catch (Exception e){
        }
        return BGZS.getString("b64tip");
    }
}
