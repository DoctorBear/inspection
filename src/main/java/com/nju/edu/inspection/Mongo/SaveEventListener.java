package com.nju.edu.inspection.Mongo;

import com.nju.edu.inspection.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
public class SaveEventListener extends AbstractMongoEventListener<Object> {
    @Autowired
    private MongoTemplate template;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event){
        if(event.getSource() instanceof Task){
            Task source = (Task)event.getSource();

            if (source != null) {
                ReflectionUtils.doWithFields(source.getClass(), field -> {
//                  暂时就task整一个id自增好了，太烦了
                    ReflectionUtils.makeAccessible(field);
                    if(field.isAnnotationPresent(AutoIncKey.class)){
                        field.set(source, TaskIncId.getInstance().getTaskIncId(template));
                    }
                });
            }
        }
    }
}
