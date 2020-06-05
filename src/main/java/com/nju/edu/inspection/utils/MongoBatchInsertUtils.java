package com.nju.edu.inspection.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MongoBatchInsertUtils {
    private static final int ThreadNum = 3;//设置向MongoDb中插入数据的线程数
    static int ThreadSizeCount = 0;//用于计算子线程完成数
    static final String COLLECTION_NAME = "filedata";//存储Collection
    private static final int batch = 2000;
    static final char sep = SeperatorsUtil.sepa;
    private String host = "localhost";
    private String taskid = "2020032504";
    private List<File> fileList;
    private String current_modifier;
    private ThreadPoolExecutor executor;

    public MongoBatchInsertUtils(String host) {
        this.host = host;
        this.fileList = new ArrayList<>();
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(ThreadNum);
    }

    public void batchInsert(String dir, String modifier, String taskid){
        Thread[] th = new Thread[ThreadNum];
        this.current_modifier = modifier;
        this.taskid = taskid;
        if (null != dir && dir.trim().length() > 0) {
            dir = SeperatorsUtil.convert(dir.trim());
            File file = new File(dir + "record.json");
            if (file.exists()) {
                findFile(dir+sep);
            }else{
                File[] flist = new File(dir).listFiles();
                System.out.println(dir);
                System.out.println(flist.length);
                if (null!=flist && flist.length>0){
                    for(File f:flist){
                        if(f.isDirectory()){
                            if (new File(f.getPath()+sep+"record.json").exists()){
                                findFile(f.getPath()+sep);
                            }else{
                                for(File son: f.listFiles()){
                                    if(son.isDirectory() && new File(son.getPath()+sep+"record.json").exists()){
                                        findFile(son.getPath()+sep);
                                    }
                                }
                            }
                        }
                    }
                    if(null!=fileList && fileList.size()!=0){
                        InsertData insertData = new InsertData(host, fileList, current_modifier, this.taskid);
                        System.out.println("剩余部分进入子线程");
                        executor.execute(insertData);
                    }
                }
            }
        }
        executor.shutdown();
        try{
            executor.awaitTermination(3L, TimeUnit.MINUTES);
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            return;
        }
    }

    private void findFile(String baseDir){
        for (File f : new File(baseDir).listFiles()) {
            if (!f.getName().equals("record.json")) {
                System.out.println(f);
                fileList.add(f);
                if (fileList.size() == batch) {
                    InsertData insertData = new InsertData(host, fileList, current_modifier, this.taskid);
                    System.out.println("findFile进入子线程");
                    executor.execute(insertData);
                    fileList = new ArrayList<>();
                }
            }
        }
    }

    public static void main(String[] args) {
        MongoBatchInsertUtils batchInsertUtils = new MongoBatchInsertUtils("localhost");
        String dir = "F:/github/inspection/src/main/resources/temp/0c927a95-4870-4a92-8fb9-aacb0f69580breformat\\";
        String username = "张三";
        String taskid = "2020042701";
        batchInsertUtils.batchInsert(dir, username, taskid);
    }
}
//消费者，向mongoDb中写数据
class InsertData implements Runnable {
    private List<File> files;
    private String current_modifier;
    private String taskid;
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    InsertData(String host, List<File> flist, String current_modifier, String taskid) {
        this.files = flist;
        this.current_modifier = current_modifier;
        this.taskid = taskid;
        this.client = MongoDBUtil.getConnect(host);
        this.db = client.getDatabase("inspection");
        this.collection = db.getCollection(MongoBatchInsertUtils.COLLECTION_NAME);
    }

    private boolean checkJson(JSONObject checked){
        if(checked==null){
            return false;
        }
        else return checked.containsKey("text_path") && checked.containsKey("absolute_path")
                && checked.containsKey("input_path") && checked.containsKey("file_name");
    }

    @Override
    public void run() {
        System.out.println("子线程时间到");
        List<Document> docList = new ArrayList<>();
        for (File file : files) {
            System.out.println(file.getName());
            if (file.isFile()) {
                if (!file.getName().equals("record.json")){
                    System.out.println(file.getName());
                    String jsonStr = JsonUtil.readFile(file.getPath());
                    JSONObject jsonObject = (JSONObject) JSON.parseObject(jsonStr);
                    if(!checkJson(jsonObject)){
                        continue;
                    }
//                    String textPath = SeperatorsUtil.convert(jsonObject.getString("text_path"));
//                    String htmlPath = SeperatorsUtil.convert(jsonObject.getString("absolute_path"));
//                    jsonObject.put("text", JsonUtil.readFile(file.getParent() + SeperatorsUtil.sepa + textPath));
//                    List<String> htmlList = new ArrayList<>();
//                    File htmlDir = new File(file.getParent() + SeperatorsUtil.sepa + htmlPath);

//                    for (File html : htmlDir.listFiles()) {
//                        if (html.getPath().endsWith(".html")) {
//                            htmlList.add(JsonUtil.readFile(html.getPath()));
//                        }
//                    }
//                    jsonObject.put("htmls", htmlList);
                    jsonObject.put("text", jsonObject.get("file_text"));
                    jsonObject.remove("file_text");

                    jsonObject.remove("text_path");
                    jsonObject.remove("absolute_path");

                    String input_path = jsonObject.getString("input_path");
                    String file_name = jsonObject.getString("file_name");
                    jsonObject.put("path", SeperatorsUtil.convert(input_path + file_name));
                    jsonObject.remove("input_path");
                    jsonObject.remove("file_name");
//                    分别为最新修改者/是否涉密/涉密描述（如果没有涉密就清空）

                    jsonObject.put("task", this.taskid);
                    jsonObject.put("creator", current_modifier);
                    jsonObject.put("record_last_modified", current_modifier);
                    jsonObject.put("secret_involved", "false");
                    jsonObject.put("secret_description", "");

                    jsonObject.put("last_modified_time", TimeUtil.getCurrentTime());

                    Document doc = Document.parse(JsonUtil.getJsonStr(jsonObject));
                    docList.add(doc);
                    System.out.println("当前doc处理完毕");
                }
            }
        }
        System.out.println("插入时间到！");
        collection.insertMany(docList, new InsertManyOptions().ordered(false));
        client.close();

    }
}


