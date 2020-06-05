package com.nju.edu.inspection.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoSingleInsertUtils {
    static final String HOST = "127.0.0.1";//主机
    static final int PORT = 27017;//端口
    static final String DATABASE_NAME = "inspection";//存储数据库名称，如果不存在会自动创建数据库
    static final String COLLECTION_NAME = "filedata";//存储Collection
    static final int batch = 2000;
    static final char sep = SeperatorsUtil.sepa;
    static final String taskid = "2020032504";
//    long startTime =  System.currentTimeMillis();
    MongoClient mongoClient = new MongoClient(HOST, PORT);
    MongoDatabase db = mongoClient.getDatabase(DATABASE_NAME);
    MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
    private List<Document> docList = new ArrayList<Document>();
    private String current_modifier = "";

    public void batchInsert(String dir, String modifier) {
        current_modifier = modifier;
        if (null != dir && dir.trim().length() > 0) {
            dir = SeperatorsUtil.convert(dir.trim());
            File file = new File(dir + "record.json");
            if (file.exists()) {
                singleInsert(dir+sep);
            }else{
                File[] flist = new File(dir).listFiles();
                System.out.println(flist);
                if (null!=flist && flist.length>0){
                    for(File f:flist){
                        System.out.println(f.getPath());
                        if(f.isDirectory() && new File(f.getPath()+sep+"record.json").exists()){
                            singleInsert(f.getPath()+sep);
                        }
                    }
                    if(null!=docList && docList.size()!=0){
                        collection.insertMany(docList);
                    }
                }
            }
        }
    }

//    重复插入的bug待处理
//    不用处理了，在MongoBatchInsert里面有处理
    public void singleInsert(String baseDir){

        File[] flist = new File(baseDir).listFiles();
        for(File file: flist){
            if(file.isFile()){
                if(file.getName().equals("record.json")){
                // 暂时没确定任务记录咋用
                }else{
                    String jsonStr = JsonUtil.readFile(file.getPath());
                    JSONObject jsonObject = (JSONObject) JSON.parseObject(jsonStr);
                    String textPath = SeperatorsUtil.convert(jsonObject.getString("text_path"));
                    String htmlPath = SeperatorsUtil.convert(jsonObject.getString("absolute_path"));
                    jsonObject.put("text", JsonUtil.readFile(baseDir+textPath));
                    List<String> htmlList = new ArrayList<>();
                    File htmlDir = new File(baseDir+htmlPath);
                    int start = 0;
                    for(File html: htmlDir.listFiles()){
                        if(html.getPath().endsWith(".html")){
                            htmlList.add(JsonUtil.readFile(html.getPath()));
                        }
                    }
                    jsonObject.put("htmls", htmlList);
                    jsonObject.remove("text_path");
                    jsonObject.remove("absolute_path");

                    String input_path = jsonObject.getString("input_path");
                    String file_name = jsonObject.getString("file_name");
                    jsonObject.put("path", SeperatorsUtil.convert(input_path+file_name));
                    jsonObject.remove("input_path");
                    jsonObject.remove("file_name");

//                    分别为最新修改者/是否涉密/涉密描述（如果没有涉密就清空）

                    jsonObject.put("task", taskid);
                    jsonObject.put("creator", current_modifier);
                    jsonObject.put("record_last_modified", current_modifier);
                    jsonObject.put("secret_involved", "false");
                    jsonObject.put("secret_description", "");

                    jsonObject.put("last_modified_time", TimeUtil.getCurrentTime());

                    Document doc = Document.parse(JsonUtil.getJsonStr(jsonObject));
                    docList.add(doc);
                    if(docList.size() >= batch){
//                        long now =  System.currentTimeMillis();
//                        System.out.println("批量插入前已耗时："+((now-startTime)/1000)+"s");
                        collection.insertMany(docList, new InsertManyOptions().ordered(false));
                        docList.clear();
//                        now =  System.currentTimeMillis();
//                        System.out.println("批量插入后已耗时："+((now-startTime)/1000)+"s");
                    }
                }
            }
        }

    }

    public void getHTML(){
        FindIterable<Document> docs = collection.find();
        MongoCursor<Document> mongoCursor = docs.iterator();

        if(mongoCursor.hasNext()){
            Document doc = mongoCursor.next();
            List<JSONObject> ja = doc.getList("htmls", JSONObject.class);
            System.out.println(ja.get(0).getString("content"));
        }else{
            System.out.println("Boom!");
        }
    }

    public static void main(String[] args) {
        String a = "D:\\inspection_test\\";
        MongoSingleInsertUtils util = new MongoSingleInsertUtils();
        util.batchInsert(a, "张三");

//        MongoSingleInsertUtils util = new MongoSingleInsertUtils();
//        util.getHTML();
    }
}
