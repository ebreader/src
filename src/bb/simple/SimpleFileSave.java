package bb.simple;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class SimpleFileSave {
    public static String readData(String filePath) throws SimpleException {
        File file=new File(filePath);
        if(!file.exists()){
            return null;
        }
        BufferedReader br=null;
        try {
            br=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            return br.readLine();
        } catch (Exception e) {
            throw new SimpleException("create stream error "+e.getMessage());
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    throw new SimpleException("close stream error "+filePath);
                }
            }
        }
    }

    public static void writeData(String filePath,String data) throws SimpleException {
        File fd=new File(filePath.substring(0,filePath.lastIndexOf(File.separator)));
        if(!fd.exists()){
            if(!fd.mkdirs()){
                throw new SimpleException("mkdirs ["+filePath+"] err");
            }
        }
        BufferedWriter br=null;
        try {
            File file=new File(filePath);
            if(!file.exists()){
                if(!file.createNewFile()){
                   throw new SimpleException("create ["+filePath+"] error");

                }
            }
            br=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
            br.write(data);
        } catch (Exception e) {
            throw new SimpleException("create stream error "+e.getMessage());
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    throw new SimpleException("close stream error "+filePath);
                }
            }
        }
    }
    public static void deleteDir(String ptd) throws SimpleException {
        Path pathToDelete = Paths.get(ptd);

        try {
            Files.walkFileTree(pathToDelete, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);  // 删除文件
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);  // 删除目录
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            throw new SimpleException("delete "+ptd+" error");
        }
    }
}
