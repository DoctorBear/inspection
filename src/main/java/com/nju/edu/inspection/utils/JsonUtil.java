package com.nju.edu.inspection.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.*;

public class JsonUtil {
    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
//            JSONObject returnData = (JSONObject)JSON.parseObject(jsonStr);
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String getJsonStr(JSONObject jsonObject){
        return jsonObject.toJSONString();
    }

    public static void main(String[] args) {
        String a = "123/";
        char last = a.charAt(a.length()-1);
        System.out.println(last!='\\');
    }
}
