package gadget;

import bb.book.BBookException;
import bb.simple.StringTool;
import bgzs.BGZS;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashConvert {
    public static String encodeB64(String input){
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeB64(String input){
        return new String(Base64.getDecoder().decode(input),StandardCharsets.UTF_8);
    }

    public static String computeHash(String input,String algorithm) throws BBookException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = digest.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BBookException(BGZS.getString("nohas"));
        }
    }

}
