package com.nju.edu.inspection.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MongoDBUtilTest {

    @Test
    void getConnect() {
        //获取数据库连接对象
        MongoClient mongoClient = MongoDBUtil.getConnect("localhost");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("inspection");
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");

        FindIterable findIterable = collection.find();
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        Bson filter = Filters.eq("name", "张三");
        FindIterable findIterable2 = collection.find(filter);
        MongoCursor cursor2 = findIterable2.iterator();
        while (cursor2.hasNext()) {
            System.out.println(cursor2.next());
        }
    }

    @Test
    void getConnect2() {
    }
}