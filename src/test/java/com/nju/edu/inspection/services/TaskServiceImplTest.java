package com.nju.edu.inspection.services;

import com.nju.edu.inspection.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class TaskServiceImplTest {
    @Autowired
    TaskServiceImpl taskService;


    @Test
    void findAll() {
        List list = taskService.findAll();
        System.out.println(list.size());
        System.out.println(list);
    }

    @Test
    void updateLastModified() {
        Task task = taskService.findTaskById("2020032504");
        taskService.updateLastModified(task, "5ea69d23e0e4d9325103e3cb", "张三");
    }

}