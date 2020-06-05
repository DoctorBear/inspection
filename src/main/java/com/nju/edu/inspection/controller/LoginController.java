package com.nju.edu.inspection.controller;

import com.nju.edu.inspection.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class LoginController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/login")
    public String login(){
        System.out.println("login in");
        return "login.html";
    }

    @RequestMapping("/check-login")
    @ResponseBody
    public String checkLogin(@RequestBody HashMap<String, String> map, HttpServletRequest request){
        String username = map.get("username");
        String password = map.get("password");
        String info = "登录成功";

        String res = userService.checkLogin(username, password);
        String[] status = res.split(":");
        if(status[0].equals("0")){
            request.getSession().setAttribute("userid", status[1]);
            request.getSession().setAttribute("username", username);
        }else{
            info = res.substring(status[0].length()+1);
        }
        System.out.println(info);
        return info;
    }


}
