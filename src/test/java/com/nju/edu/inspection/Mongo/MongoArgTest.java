package com.nju.edu.inspection.Mongo;

import com.nju.edu.inspection.utils.MongoArg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoArgTest {

    @Autowired
    MongoArg mongoArg;

    @Test
    public void mongoPath(){
        System.out.println(mongoArg.getPath());
        System.out.println(mongoArg.getHost());
        System.out.println(mongoArg.getDatabase());
        System.out.println(mongoArg.getPort());
    }
}