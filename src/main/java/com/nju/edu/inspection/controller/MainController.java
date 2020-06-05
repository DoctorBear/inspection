package com.nju.edu.inspection.controller;

import com.nju.edu.inspection.entity.FileData;
import com.nju.edu.inspection.utils.MongoArg;
import com.nju.edu.inspection.services.FileServiceImpl;
import com.nju.edu.inspection.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private TmpDirUtil tmpDirUtil;

    @Autowired
    private MongoArg mongoArg;

    @RequestMapping("/main")
    public String getTask(){
        return "main.html";
    }

    @RequestMapping("/get-hosts")
    @ResponseBody
    public List<String> getHosts(@RequestParam String taskid) {
        return fileService.getHosts(taskid);
    }

    @RequestMapping("/get-files")
    @ResponseBody
    public List<FileData> getFiles(@RequestParam String taskid){
        return fileService.findFiles(taskid);
    }

    @RequestMapping("/get-files-by-host")
    @ResponseBody
    public List<FileData> getFilesByHost(@RequestParam String taskid, @RequestParam String uuid){
        return fileService.findFilesByHost(taskid, uuid);
    }

    @RequestMapping("/upload-file")
    @ResponseBody
    public String uploadFile(@RequestParam("file")MultipartFile file, @RequestParam String taskid, HttpServletRequest request){
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        if(file.isEmpty()){
            return "文件为空";
        }
        String fileName = file.getOriginalFilename();
        fileName = UUID.randomUUID().toString()+fileName;
        System.out.println(fileName);
        System.out.println(taskid);

        String path = tmpDirUtil.getLocation()+File.separator+fileName;
        File dest = new File(path);
        try{
            file.transferTo(dest);
        } catch (IOException e){
            return "上传失败";
        }
        String destDir = path.substring(0,path.length()-4);
        ZipUtil.unZipFiles(path,destDir);
        System.out.println("解压完毕");
        MongoBatchInsertUtils insertUtils = new MongoBatchInsertUtils(mongoArg.getHost());
        insertUtils.batchInsert(destDir, username, taskid);
        dest.delete();
//        todo 死在了这一行，应该是因为在这里跑的时候子线程还在跑
        FileUtil.deleteDir(destDir);
        return "上传成功";
    }

    @RequestMapping("/upload-trunk")
    public String uploadTrunk(){
        return null;
    }

    @RequestMapping("/merge")
    public String mergeTrunk(){
        return null;
    }

    @RequestMapping("/search-files")
    @ResponseBody
    public List<FileData> searchFiles(@RequestBody HashMap<String, String> map){
        String taskid = map.get("taskid");
        String keywords = map.get("keywords");
        String hostid = map.get("hostid");
        return fileService.searchFiles(taskid, hostid, keywords);
    }

    @RequestMapping("/export-files")
    @ResponseBody
    public String exportFiles(@RequestParam String taskid, HttpServletResponse response){
        String filePath = fileService.exportFiles(taskid);
        if(!filePath.equals("导出失败")){
            String[] parts = filePath.split("\\\\");
            String filename = parts[parts.length-1];
            File file = new File(filePath);
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName="+filename);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while(i != -1){
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                outputStream.flush();
                outputStream.close();
                return "下载成功";
            } catch(Exception e){
                e.printStackTrace();
            } finally {
                if(bis!=null){
                    try{
                        bis.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                if(fis!=null){
                    try{
                        fis.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                file.delete();
            }
            return "下载失败";
        }else{
            return "导出失败";
        }
    }

    @RequestMapping("/generate-report")
    @ResponseBody
    public String generateReport(@RequestParam String taskid, HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        String filePath = fileService.generateReport(username, taskid);
        if(!filePath.equals("导出失败")){
            String[] parts = filePath.split("\\\\");
            String filename = parts[parts.length-1];
            File file = new File(filePath);
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName="+filename);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while(i != -1){
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                outputStream.flush();
                outputStream.close();
                return "下载成功";
            } catch(Exception e){
                e.printStackTrace();
            } finally {
                if(bis!=null){
                    try{
                        bis.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                if(fis!=null){
                    try{
                        fis.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                file.delete();
            }
            return "下载失败";
        }else{
            return "导出失败";
        }
    }
}
