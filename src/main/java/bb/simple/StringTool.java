package bb.simple;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public  class StringTool {
    public static String exceptionStack(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }



    public static String md5(String input){
        try {
            // 获取 MessageDigest 实例，指定使用 MD5 算法
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组
            byte[] inputBytes = input.getBytes();

            // 计算 MD5 哈希值
            byte[] digest = md.digest(inputBytes);

            // 将字节数组转换为十六进制的字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                // 将每个字节转换为两位十六进制数，并追加到结果字符串中
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(input.hashCode());
        }
    }

    private static final String BASE62_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int BASE62_SIZE = BASE62_ALPHABET.length();

    // 将字节数组转换为 Base62 编码的字符串
    public static String encodeB2(byte[] input) {
        // 将字节数组转换为一个大整数
        long value = 0;
        for (byte b : input) {
            value = (value << 8) | (b & 0xFF); // 将字节按位拼接成一个长整型数值
        }

        // 将大整数转换为 Base62 字符串
        StringBuilder encoded = new StringBuilder();
        if (value == 0) {
            encoded.append(BASE62_ALPHABET.charAt(0)); // 如果输入是空或零，直接返回字符0
        } else {
            while (value > 0) {
                int remainder = (int) (value % BASE62_SIZE);
                encoded.insert(0, BASE62_ALPHABET.charAt(remainder)); // 插入字符
                value = value / BASE62_SIZE;
            }
        }

        return encoded.toString();
    }

    // 将 Base62 编码的字符串解码为字节数组
    public static byte[] decodeB62(String encoded) {
        long decodedValue = 0;
        for (int i = 0; i < encoded.length(); i++) {
            char c = encoded.charAt(i);
            int index = BASE62_ALPHABET.indexOf(c);
            if (index == -1) {
                throw new IllegalArgumentException("Invalid Base62 character: " + c);
            }
            decodedValue = decodedValue * BASE62_SIZE + index;
        }

        // 将长整型值转换为字节数组
        byte[] decodedBytes = new byte[Long.BYTES]; // 长整型最多需要 8 个字节
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            decodedBytes[i] = (byte) (decodedValue & 0xFF);
            decodedValue >>= 8;
        }

        // 去掉前面的零字节
        int nonZeroIndex = 0;
        while (nonZeroIndex < decodedBytes.length && decodedBytes[nonZeroIndex] == 0) {
            nonZeroIndex++;
        }

        return Arrays.copyOfRange(decodedBytes, nonZeroIndex, decodedBytes.length);
    }
    public static String detectEncoding(byte[] data) throws SimpleException {
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(data, 0, data.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();

        if (encoding != null) {
            return encoding;
        } else {
            throw new SimpleException("unknown char set");
        }
    }



    public static List<Pattern> parseRegexToPattern(String selectText){
        List<Pattern> plist=new ArrayList<>();
        List<String> s=parseRegex(selectText);
        for(String ss:s){
            plist.add(Pattern.compile(ss));
        }
        return plist;
    }

    public static List<String> parseRegex(String selectText){
        List<String> li=new ArrayList<>();
        int startIndex=selectText.indexOf("(#");
        int endIndex=selectText.indexOf("#)");
        while(startIndex>=0){
            String regex=selectText.substring(startIndex+2,endIndex);
            li.add(regex);
            selectText=selectText.replace(regex,"").replace("(##)","");
            startIndex=selectText.indexOf("(#");
            endIndex=selectText.indexOf("#)");
        }
        li.add(0,selectText);
        return li;
    }
}