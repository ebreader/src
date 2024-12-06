package gadget;

import bb.book.BBookException;

public class TimestampButton extends GadgetButton{
    @Override
    protected String executeFunction(String inputText) throws BBookException {
        return TimestampConvert.convert(inputText);
    }
}
