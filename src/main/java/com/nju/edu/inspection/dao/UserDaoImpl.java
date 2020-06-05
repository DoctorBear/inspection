package com.nju.edu.inspection.dao;

import com.nju.edu.inspection.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl {
    @Autowired
    private MongoTemplate template;

    public void saveUser(User user){
        template.save(user);
    }

    /**
     * 根据用户名查询对象
     * @return
     */
    public List<User> findUserByName(String name) {
        Query query=new Query(Criteria.where("name").is(name));
        List<User> list = template.find(query, User.class);
        return list;
    }

    /**
     * 更新对象
     */
    public void updatePassword(User user) {
//        Query query=new Query(Criteria.where("id").is(user.getId()));
//        Update update= new Update().set("password", user.getPassword());
//        //更新查询返回结果集的第一条
//        mongoTemplate.updateFirst(query,update,User.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,TestEntity.class);
    }

    /**
     * 删除对象
     * @param id
     */
    public void deleteTestById(Integer id) {
        Query query=new Query(Criteria.where("id").is(id));
        template.remove(query,User.class);
    }

    public void findAll() {
        List<User> all = template.findAll(User.class,"user");
        System.out.println(all);
    }
}
