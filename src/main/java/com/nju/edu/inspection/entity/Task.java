package com.nju.edu.inspection.entity;

import com.nju.edu.inspection.Mongo.AutoIncKey;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "task")
public class Task {
    @Id
    @AutoIncKey
    private String id;
    private String name;
    private Date createdTime;
    private String creator;
    private String creatorName;
    private Date lastModiedTime;
    private String lastModified;
    private String lastModifiedName;

    public Task(String id, String name, Date createdTime, String creator, String creatorName, Date lastModiedTime, String lastModified, String lastModifiedName) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
        this.creator = creator;
        this.creatorName = creatorName;
        this.lastModiedTime = lastModiedTime;
        this.lastModified = lastModified;
        this.lastModifiedName = lastModifiedName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdTime=" + createdTime +
                ", creator='" + creator + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", lastModiedTime=" + lastModiedTime +
                ", lastModified='" + lastModified + '\'' +
                ", lastModifiedName='" + lastModifiedName + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getLastModiedTime() {
        return lastModiedTime;
    }

    public void setLastModiedTime(Date lastModiedTime) {
        this.lastModiedTime = lastModiedTime;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedName() {
        return lastModifiedName;
    }

    public void setLastModifiedName(String lastModifiedName) {
        this.lastModifiedName = lastModifiedName;
    }
}
