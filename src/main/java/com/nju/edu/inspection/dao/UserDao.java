package com.nju.edu.inspection.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nju.edu.inspection.utils.MongoDBUtil;
import org.bson.Document;

public class UserDao {

    public static void insertOne(Document document) {
        //获取数据库连接对象
        MongoClient client = MongoDBUtil.getConnect("localhost");
        MongoDatabase mongoDatabase = client.getDatabase("inspection");
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");
        //插入一个文档
        collection.insertOne(document);
    }
}
