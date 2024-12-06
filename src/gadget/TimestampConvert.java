package gadget;

import bb.book.BBookException;
import bgzs.BGZS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConvert {
    private static final SimpleDateFormat A=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat B=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat C=new SimpleDateFormat("yyyy-MM-dd HH");
    private static final SimpleDateFormat D=new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat E=new SimpleDateFormat("yyyy-MM");

    public static String convert(String value){
        if(value==null||value.equals("")){
            try {
                return BGZS.getString("timestamptip");
            } catch (BBookException e) {
                return e.msg;
            }
        }
        try{
            return A.format(new Date(Long.parseLong(value)));
        }catch (Exception e){
            Date date=getDate(value);


            if(date==null){
                try {
                    return BGZS.getString("timestamptip");
                } catch (BBookException ex) {
                    return ex.msg;
                }
            }
            return String.valueOf(date.getTime());
        }
    }
    private static Date getDate(String value){

        try {
            return A.parse(value);
        } catch (ParseException ex) {
        }
        try {
            return B.parse(value);
        } catch (ParseException ex) {
        }
        try {
            return C.parse(value);
        } catch (ParseException ex) {
        }

        try {
            return D.parse(value);
        } catch (ParseException ex) {
        }

        try {
            return E.parse(value);
        } catch (ParseException ex) {
        }

        return null;
    }
}
