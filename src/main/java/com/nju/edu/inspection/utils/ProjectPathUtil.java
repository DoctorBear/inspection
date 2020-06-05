package com.nju.edu.inspection.utils;

import java.io.File;
import java.io.IOException;

public class ProjectPathUtil {
    public static String getResourcePath(){
        try{
            File file = new File("src/main/resources");
            return file.getCanonicalPath();
        } catch(IOException e){
            e.printStackTrace();
        }
        return "获取失败";
    }

    public static void main(String[] args) {
        System.out.println(ProjectPathUtil.getResourcePath());
    }
}
