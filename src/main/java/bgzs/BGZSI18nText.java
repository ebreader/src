package bgzs;

import bb.simple.I18nText;

import java.util.ResourceBundle;

public class BGZSI18nText implements I18nText {
    private ResourceBundle bundle;

    public BGZSI18nText(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public String getString(String key) {
        try{
            return bundle.getString(key);
        }catch (Exception e){
            return key;

        }
    }
}
