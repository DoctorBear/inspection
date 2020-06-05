package com.nju.edu.inspection.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {
    private static final int BUFFER_SIZE = 2 * 1024;
    private static final boolean KeepDirStructure = true;

    @SuppressWarnings({ "rawtypes", "resource" })
    public static void unZipFiles(String zipPath, String descDir) {
        long start = System.currentTimeMillis();
        try{
            File zipFile = new File(zipPath);
            System.err.println(zipFile.getName());
            if(!zipFile.exists()){
                throw new IOException("需解压文件不存在.");
            }
            File pathFile = new File(descDir);
            System.out.println("descdir: "+descDir);
            if (!pathFile.exists()) {
                System.out.println("boom?");
                System.out.println(pathFile.mkdir());
            }
            System.out.println("这玩意儿真的创建成功了？"+pathFile.exists());
            ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
            for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                System.out.println(zipEntryName);
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir + File.separator + zipEntryName).replaceAll("\\*", "/");
                System.err.println(outPath);
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                // 输出文件路径信息
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String zipPath = ProjectPathUtil.getResourcePath()+"\\temp\\"+"0c927a95-4870-4a92-8fb9-aacb0f69580breformat.zip";
        String destDir = zipPath.substring(0,zipPath.length()-4);
        ZipUtil.unZipFiles(zipPath, destDir);
    }
}
