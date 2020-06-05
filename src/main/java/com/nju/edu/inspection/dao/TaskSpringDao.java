package com.nju.edu.inspection.dao;

import com.nju.edu.inspection.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskSpringDao {
    @Autowired
    private MongoTemplate template;

    public void saveTask(Task task){template.save(task);}

    public Task findTaskById(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        Task task =  template.findOne(query , Task.class);
        return task;
    }

    public Task findTaskByDate(String date){
        return null;
    }

    public List<Task> findAll() {
        List<Task> all = template.findAll(Task.class, "task");
        return all;
    }

    public void updateLastModified(Task task){
        Query query=new Query(Criteria.where("id").is(task.getId()));
        Update update = new Update().set("lastModified", task.getLastModified())
                .set("lastModifiedName", task.getLastModifiedName())
                .set("lastModiedTime", task.getLastModiedTime());
        template.updateFirst(query, update, Task.class);
    }

    public void deleteTaskById(String id) {
        Query query=new Query(Criteria.where("id").is(id));
        template.remove(query,Task.class);
    }
}
