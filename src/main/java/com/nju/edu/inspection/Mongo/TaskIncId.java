package com.nju.edu.inspection.Mongo;

import com.nju.edu.inspection.entity.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TaskIncId {
    private static TaskIncId taskIncId = null;

    private TaskIncId(){}

    static synchronized TaskIncId getInstance() {
        if (taskIncId == null) {
            synchronized (TaskIncId.class) {
                if (taskIncId == null) {
                    taskIncId = new TaskIncId();
                }
            }
        }
        return taskIncId;
    }

    //    不是单纯的数字id自增，而是日期加上当天创建的第几个
    synchronized String getTaskIncId(MongoTemplate template) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = date.format(fmt);
        Query query = new Query(Criteria.where("id").regex(dateStr + "\\d*")).with(Sort.by(Direction.DESC, "id"));
        List<Task> list = template.find(query, Task.class);
        if (list != null && !list.isEmpty()) {
            String numStr = list.get(0).getId().substring(8);
            Integer numInteger = new Integer(numStr);
            int num = numInteger.intValue();
            return dateStr + (++num < 10 ? "0" + num : num);
        } else {
            return dateStr + "01";
        }
    }
}
