package com.nju.edu.inspection.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MongoExportUtil {
    private String dbPath;
    private String host;//主机
    private int port;//端口
    private String dbName;//存储数据库名称，如果不存在会自动创建数据库
    private MongoClient mongoClient;
    private MongoDatabase db;

    public MongoExportUtil(String dbPath, String host, int port, String dbName) {
        this.dbPath = dbPath;
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        mongoClient = new MongoClient(host, port);
        db = mongoClient.getDatabase(dbName);
    }

//    先把默认参数为任务放这儿，要其它导出方式的话另说
    public int exportData(String taskid, String outputPath){
        String query = String.format("\"{\'task\':\'%s\'}\"", taskid);
        String[] cmd = {dbPath, "--host", host, "-d", dbName, "-c", "filedata", "-q", query, "-o", outputPath};
        try{
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while((line = br.readLine())!=null){
                System.out.println(line);
            }
            return process.waitFor();

        } catch (InterruptedException | IOException e){
            e.printStackTrace();
            System.out.println(e.toString());
            return -1;
        }
    }

    //    mongoexport --host 127.0.0.1:27017 -d inspection -c filedata -q "{'uuid':'VQ2MT4BH2PY'}" -o C:\Users\xiongkaiqi\Desktop\测试文件夹\1.json(date+%F).json
    public static void main(String[] args) {
        String mongoPath = "C:\\Program Files\\MongoDB\\Server\\4.0\\bin\\";
        String dbname = "inspection";
        String colname = "filedata";
        String host = "127.0.0.1:27017";
        String taskid = "2020032504";
        String query = String.format("\"{\'task\':\'%s\'}\"", taskid);
        String outputPath = "C:/Users/xiongkaiqi/Desktop/测试文件夹/1.json";
        String[] cmd = {mongoPath+"mongoexport.exe", "--host", host, "-d", dbname, "-c", colname, "-q", query, "-o", outputPath};
        try{
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while((line = br.readLine())!=null){
                System.out.println(line);
            }
            System.out.println(process.waitFor());

        } catch (InterruptedException | IOException e){
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }
}
