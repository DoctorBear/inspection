package com.nju.edu.inspection.services;

import com.nju.edu.inspection.dao.TaskSpringDao;
import com.nju.edu.inspection.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl {

    @Autowired
    private TaskSpringDao dao;

    public void createTask(String taskName, String creatorId ,String creatorName){
        Date now = new Date();
        Task task = new Task(null, taskName, now, creatorId, creatorName, now, creatorId, creatorName);
        dao.saveTask(task);
        System.out.println("保存成功");
    }

    public List<Task> findAll(){
        List<Task> tasks = dao.findAll();
//        按照创建顺序
        Collections.reverse(tasks);
        return tasks;
    }

    public Task findTaskById(String id){
        return dao.findTaskById(id);
    }

    public void updateLastModified(Task task, String lastModified, String name){
        task.setLastModified(lastModified);
        task.setLastModifiedName(name);
        task.setLastModiedTime(new Date());
        dao.updateLastModified(task);
    }
}
