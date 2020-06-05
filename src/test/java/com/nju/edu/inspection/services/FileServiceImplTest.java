package com.nju.edu.inspection.services;

import com.nju.edu.inspection.entity.FileData;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileServiceImplTest {
    @Autowired
    FileServiceImpl fileService;

    @Test
    void findById() {
        FileData fileData = fileService.findById("5ea69d23e0e4d9325103e3cb");
        System.out.println(fileData.getHtmls().get(0));
    }

    @Test
    void exportReport() {
        String taskid = "2020032504";
        String str = fileService.generateReport("张三",taskid);
        System.out.println(str);
    }
}