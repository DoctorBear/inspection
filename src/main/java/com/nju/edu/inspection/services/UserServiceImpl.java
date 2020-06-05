package com.nju.edu.inspection.services;

import com.nju.edu.inspection.dao.UserDaoImpl;
import com.nju.edu.inspection.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl {

    @Autowired
    private UserDaoImpl userDaoImpl;

    /**
     * template方式
     */
    public void save(User user){
        userDaoImpl.saveUser(user);
        System.out.println("保存成功");
    }

    public String checkLogin(String name,String password){
        List<User> users = userDaoImpl.findUserByName(name);
//        List<String> passwords = new ArrayList<>();
//        for(User user:users){
//            if(user.getPassword().equals(password)){
//
//                return true;
//            }
//        }
        if(users.size()==0){
//            todo 看起来得整一枚举类,有空再弄吧
            return "2:用户不存在";
        }
        User user = users.get(0);
        if(user.getPassword().equals(password)){
            return "0:"+user.getId();
        }
        return "1:密码错误";
    }
}
