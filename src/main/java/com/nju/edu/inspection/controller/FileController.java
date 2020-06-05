package com.nju.edu.inspection.controller;

import com.nju.edu.inspection.entity.FileData;
import com.nju.edu.inspection.services.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class FileController {

    @Autowired
    private FileServiceImpl fileService;

    @RequestMapping("/file")
    public String getFile() {
        System.out.println("turn to file!");
        return "file.html";
    }

    @RequestMapping("/get-file")
    @ResponseBody
    public FileData getSpecificFile(@RequestBody HashMap<String, String> map){
        String file_id = map.get("file_id");
        return fileService.findById(file_id);
    }

    @RequestMapping("/comment")
    @ResponseBody
    public String updateComment(@RequestBody HashMap<String, String> map, HttpServletRequest request){
        HttpSession session = request.getSession();
        String userid = (String)session.getAttribute("username");

        String fileid = map.get("file_id");
        String involved = map.get("involved");
        String description = map.get("description");
        return fileService.updateComment(userid, fileid, involved, description);
    }

}
