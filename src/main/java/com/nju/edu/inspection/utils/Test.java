package com.nju.edu.inspection.utils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;


public class Test {

    public static void main(String[] args) throws IOException {
        int pageSize=10000;

        try {
            MongoClient mongo = new MongoClient("localhost", 27017);

            /**** Get database ****/
            // if database doesn't exists, MongoDB will create it for you
            MongoDatabase db = mongo.getDatabase("www");

            /**** Get collection / table from 'testdb' ****/
            // if collection doesn't exists, MongoDB will create it for you
            MongoCollection table = db.getCollection("person");

            DBCursor dbObjects;
            Long cnt=table.countDocuments();
            Long page=getPageSize(cnt,pageSize);
            ObjectId lastIdObject=null;
            Long start=System.currentTimeMillis();
            long ss=start;
            for(Long i=0L;i<page;i++) {
                start=System.currentTimeMillis();
                dbObjects=getCursorForCollection(table, lastIdObject, pageSize);
                System.out.println("第"+(i+1)+"次查询，耗时:"+(System.currentTimeMillis()-start)+" 毫秒");
                List<DBObject> objs=dbObjects.toArray();
                start=System.currentTimeMillis();
                batchInsertToEsSync();
                lastIdObject=(ObjectId) objs.get(objs.size()-1).get("_id");
                System.out.println("第"+(i+1)+"次插入，耗时:"+(System.currentTimeMillis()-start)+" 毫秒");
            }
            System.out.println("耗时:"+(System.currentTimeMillis()-ss)/1000+"秒");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }


    }

    public static void batchInsertToEsSync() throws IOException {

    }

    public static DBCursor getCursorForCollection(MongoCollection collection,ObjectId lastIdObject,int pageSize) {
        DBCursor dbObjects=null;
//        if(lastIdObject==null) {
//            lastIdObject=(ObjectId) collection.findOne().get("_id");
//        }
//        BasicDBObject query=new BasicDBObject();
//        query.append("_id",new BasicDBObject("$gt",lastIdObject));
//        BasicDBObject sort=new BasicDBObject();
//        sort.append("_id",1);
//        dbObjects=collection.find(query).limit(pageSize).sort(sort);
        return dbObjects;
    }

    public static Long getPageSize(Long cnt,int pageSize) {
        return cnt%pageSize==0?cnt/pageSize:cnt/pageSize+1;
    }
}
