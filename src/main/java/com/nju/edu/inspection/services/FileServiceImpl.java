package com.nju.edu.inspection.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nju.edu.inspection.dao.FileDataDaoImpl;
import com.nju.edu.inspection.dao.TaskSpringDao;
import com.nju.edu.inspection.entity.FileData;
import com.nju.edu.inspection.utils.MongoArg;
import com.nju.edu.inspection.entity.Task;
import com.nju.edu.inspection.utils.MongoExportUtil;
import com.nju.edu.inspection.utils.ProjectPathUtil;
import com.nju.edu.inspection.utils.TmpDirUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.*;

@Service
public class FileServiceImpl {
    @Autowired
    private FileDataDaoImpl dao;
    @Autowired
    private TaskSpringDao taskSpringDao;

    @Autowired
    private MongoArg mongoArg;

    public String uploadFile(String path, String taskid, String userid){
        return "";
    }

    public FileData findById(String file_id){
        FileData fileData = dao.findById(file_id);
        List<String> htmls = fileData.getHtmls();
        List<String> parsedHtmls = new ArrayList<>();
        for(String html:htmls){
            Document document = Jsoup.parse(html);
//            System.out.println(document.getElementsByClass("container").html());
            Elements elements = document.getElementsByClass("container").get(0).children();
            StringBuilder tmp = new StringBuilder();
            for(int i=0;i<elements.size();i++){
                if(i>=3){
                    tmp.append(elements.get(i).toString());
                }
            }
            parsedHtmls.add(tmp.toString());
        }
        fileData.setHtmls(parsedHtmls);

        //像js一样，通过标签获取title
//        System.out.println(document.getElementsByTag("title").first());
        return fileData;
    }

    public boolean MongoSingleInsertUtils(){
        return true;
    }

    public String exportFiles(String taskid){
        MongoExportUtil exportUtil = new MongoExportUtil(mongoArg.getPath(), mongoArg.getHost(), mongoArg.getPort(), mongoArg.getDatabase());
        String prefixPath = ProjectPathUtil.getResourcePath()+File.separator+"temp"+ File.separator;
//        String prefixPath = tmpDirUtil.getLocation()+File.separator;
        String fileName = prefixPath+UUID.randomUUID().toString();
        String outputPath = fileName+".json";
        int status = exportUtil.exportData(taskid, outputPath);
        if(status==0){
            return outputPath;
        }else{
            System.out.println("导出失败");
            return "导出失败";
        }

    }

    public List<String> getHosts(String taskid){
        return dao.getHosts(taskid);
    }

    public List<FileData> findFilesByHost(String taskid, String uuid){
        return dao.getFilesByHost(taskid, uuid);
    }

    public List<FileData> findFiles(String taskid){
        return dao.getFiles(taskid);
    }

    public String updateComment(String username, String fileid, String involved, String description){
        return dao.updateComment(username, fileid, involved, description);
    }

    public List<FileData> searchFiles(String taskid, String hostid, String keywords){
        String[] words = keywords.split(" ");
        return dao.searchFiles(taskid, hostid, words);
    }

    public String generateReport(String username, String taskid){
        List<FileData> list = dao.findInvolvedFiles(taskid);
        JSONObject report = new JSONObject();
        report.put("任务id",taskid);
        Task task = taskSpringDao.findTaskById(taskid);
        report.put("任务名", task.getName());
        report.put("创建者", username);
        Map<String, JSONArray> map = new HashMap<String, JSONArray>();
        JSONArray hosts = new JSONArray();
        for(FileData fileData: list){
            String uuid = fileData.getUuid();
            JSONObject file = new JSONObject();
            file.put("文件路径", fileData.getPath());
            file.put("检查者", fileData.getRecord_last_modified());
            file.put("检查日期", fileData.getLast_modified_time());
            file.put("涉密描述", fileData.getSecret_description());
            if(map.keySet().contains(uuid)){
                JSONArray jsonArray = map.get(uuid);
                jsonArray.add(file);
            }else{
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(file);
                map.put(uuid, jsonArray);
            }
        }
        for(Map.Entry entry:map.entrySet()){
            JSONObject host = new JSONObject();
            host.put(entry.getKey().toString(), entry.getValue());
            hosts.add(host);
        }
        report.put("涉密主机", hosts);
        String prefixPath = ProjectPathUtil.getResourcePath()+File.separator+"temp"+File.separator;
        String fileName = prefixPath+UUID.randomUUID().toString();
        String outputPath = fileName+".json";
        this.jsonWrite(report, outputPath);
        return outputPath;
    }

    private void jsonWrite(JSONObject jsonObject, String outputPath){

        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputPath),"UTF-8");
            osw.write(jsonObject.toJSONString());
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
