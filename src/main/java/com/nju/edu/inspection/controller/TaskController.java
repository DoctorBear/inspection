package com.nju.edu.inspection.controller;

import com.nju.edu.inspection.utils.MongoArg;
import com.nju.edu.inspection.entity.Task;
import com.nju.edu.inspection.services.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private MongoArg mongoArg;



    @RequestMapping("/task")
    public String getTask(){
        return "task.html";
    }

    @RequestMapping("/get-task-list")
    @ResponseBody
    public List<Task> getTaskList(){
        List<Task> taskList = taskService.findAll();
        for(Task task:taskList){
            System.out.println(task.getName());
        }
        return taskList;
    }

    @RequestMapping("/create-task")
    @ResponseBody
    public String createTask(@RequestBody HashMap<String, String> map, HttpServletRequest request){
        HttpSession session = request.getSession();
        String userid = (String)session.getAttribute("userid");
        String userName = (String)session.getAttribute("username");
        System.out.println("userName: "+userName);
        taskService.createTask(map.get("name"), userid, userName);
        return "创建成功";
    }




}
