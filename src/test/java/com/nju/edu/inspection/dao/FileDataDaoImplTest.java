package com.nju.edu.inspection.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nju.edu.inspection.entity.FileData;
import com.nju.edu.inspection.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileDataDaoImplTest {
    @Autowired
    FileDataDaoImpl dao;

    @Test
    void getHtml() {
        FileData fd = dao.getHtml("5ea69d23e0e4d9325103e3cb");
        System.out.println(fd.getHtmls().get(0));
        System.out.println(fd.getLast_modified_time());
    }

    @Test
    void findSimplifiedById() {
        FileData fd = dao.findSimplifiedById("5ea69d23e0e4d9325103e3cb");
        System.out.println(fd.getId());
        System.out.println(fd.getMac());
        System.out.println(fd.getPath());
    }

    @Test
    void getHosts(){
        List<String> hosts = dao.getHosts("2020032504");
        for(String uuid:hosts){
            System.out.println(uuid);
        }
    }

    @Test
    void getFiles(){
        List<FileData> list = dao.getFiles("2020032504");
        System.out.println(list.size());
    }

    @Test
    void updateComment(){
        String id = "5ea69d23e0e4d9325103e3cb";
        String secret_involved = "true";
        String description = "大概是涉密了";
        String username = "王五";
        String s = dao.updateComment(username, id, secret_involved, description);
        System.out.println(s);
    }

    @Test
    void searchDocs(){
        String taskid = "2020032504";
        String[] tags = new String[]{"屏蔽袋", "按扣"};
        String hostid = null;
        List<FileData> list = dao.searchFiles(taskid, hostid, tags);
        System.out.println(list.size());
    }

    @Test
    void getInvolvedFiles(){
        String taskid = "2020032504";
        List<FileData> list = dao.findInvolvedFiles(taskid);
        System.out.println(list.get(0).toString());
    }
}