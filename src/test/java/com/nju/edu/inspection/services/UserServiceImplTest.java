package com.nju.edu.inspection.services;

import com.nju.edu.inspection.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void contextLoads() {
        save();
    }

    @Test
    private void save() {
        User user = new User();
        user.setName("张三");
        user.setPassword("123");
        userService.save(user);
    }


}