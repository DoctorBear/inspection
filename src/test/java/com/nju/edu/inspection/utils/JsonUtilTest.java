package com.nju.edu.inspection.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

class JsonUtilTest {

    @Test
    void readJsonFile() {
        String path = "C:\\Users\\xiongkaiqi\\Desktop\\测试文件夹\\record.json";
        String jsonStr = JsonUtil.readFile(path);
        JSONObject jo = (JSONObject)JSON.parseObject(jsonStr);
        System.out.println(jo.get("input_path"));
        jo.put("taskid", '1');
        System.out.println(JsonUtil.getJsonStr(jo));
    }
}