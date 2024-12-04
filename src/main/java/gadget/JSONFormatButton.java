package gadget;

import bb.book.BBookException;
import bgzs.BGZS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JSONFormatButton extends GadgetButton{
    @Override
    protected String executeFunction(String inputText) throws BBookException {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Object jsonObject = mapper.readValue(inputText, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(jsonObject);
        }catch (Exception e){
        }
        return BGZS.getString("jsonerror");
    }
}
