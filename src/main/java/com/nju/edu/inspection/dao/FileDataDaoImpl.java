package com.nju.edu.inspection.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.client.result.UpdateResult;
import com.nju.edu.inspection.entity.FileData;
import com.nju.edu.inspection.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class FileDataDaoImpl {
    @Autowired
    private MongoTemplate template;

    public FileData getHtml(String id){
        Query query = new Query(Criteria.where("id").is(id));
        query.fields().include("htmls");
        System.out.println(query.toString());

        return template.find(query, FileData.class).get(0);
    }

    public FileData findById(String id){
        Query query = new Query(Criteria.where("id").is(id));
        return template.findById(id, FileData.class);
    }

    public FileData findSimplifiedById(String id){
        Query query = new Query(Criteria.where("id").is(id));
        query.fields().exclude("pages");
        query.fields().exclude("htmls");
        query.fields().exclude("text");
        return template.findOne(query, FileData.class);
    }

    public List<String> getHosts(@RequestParam String taskid){
        Query query = new Query(Criteria.where("task").is(taskid));
        List<String> hosts = template.findDistinct(query, "uuid", FileData.class, String.class);
        return hosts;
    }

    public List<FileData> getFilesByHost(String taskid, String uuid){
        Query query = new Query();
        query.addCriteria(Criteria.where("task").is(taskid));
        query.addCriteria(Criteria.where("uuid").is(uuid));
        query.limit(1000);
        query.fields().exclude("pages");
        query.fields().exclude("htmls");
        query.fields().exclude("text");
        List<FileData> files = template.find(query, FileData.class);
        return files;
    }

    public List<FileData> getFiles(String taskid){
        Query query = new Query();
        query.addCriteria(Criteria.where("task").is(taskid));
        query.limit(1000);
        query.fields().exclude("pages");
        query.fields().exclude("htmls");
        query.fields().exclude("text");
        List<FileData> files = template.find(query, FileData.class);
        return files;
    }

    public String updateComment(String username, String fileid, String secret_involved, String secret_description) {
        System.out.println(fileid+":updated!");
        Query query = Query.query(Criteria.where("id").is(fileid));
        Update update = new Update();
        update.set("record_last_modified", username);
        update.set("secret_involved", secret_involved);
        update.set("secret_description",secret_description);
        update.set("last_modified_time", TimeUtil.getCurrentTime());
        UpdateResult result = template.updateFirst(query, update, FileData.class);
        System.out.println(result.getMatchedCount());
        if(result.getModifiedCount()>0){
            return "更新成功";
        }else{
            return "更新失败";
        }
    }

    public List<FileData> searchFiles(String taskid, String hostid, String[] keywords){
        StringBuilder patternBuilder = new StringBuilder(".*");
        for(String s:keywords){
            patternBuilder.append(s);
            patternBuilder.append(".*");
        }
        System.out.println(patternBuilder.toString());
        Pattern pattern = Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE);
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("path").regex(pattern),
                Criteria.where("text").regex(pattern));
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("task").is(taskid));
        if(hostid!=null){
            query.addCriteria(Criteria.where("uuid").is(hostid));
        }
        System.out.println(query.toString());
        query.fields().exclude("pages");
        query.fields().exclude("htmls");
        query.fields().exclude("text");
        query.limit(1000);
        List<FileData> files = template.find(query, FileData.class);

        return files;
    }

    public List<FileData> findInvolvedFiles(String taskid){
        Query query = new Query(Criteria.where("task").is(taskid));
        query.addCriteria(Criteria.where("secret_involved").is("true"));
        query.fields().include("id");
        query.fields().include("task");
        query.fields().include("uuid");
        query.fields().include("path");
        query.fields().include("record_last_modified");
        query.fields().include("last_modified_time");
        query.fields().include("secret_description");
        return template.find(query, FileData.class);
    }
}
