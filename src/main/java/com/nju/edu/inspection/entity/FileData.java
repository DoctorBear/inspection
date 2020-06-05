package com.nju.edu.inspection.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "filedata")
public class FileData {
    @Id
    private String id;

    private String uuid;
    private String mac;
    private String path;
    private int page_count;
    private String created_time;
    private String creator;
    private String last_modified_time;
    private List<Page> pages;
    private List<String> htmls;
    private String text;
    private String secret_involved;
    private String secret_description;
    private String record_last_modified;
    private String task;

    public FileData() {

    }

    public FileData(String id, String uuid, String mac, String path, int page_count, String created_time, String creator, String last_modified_time, List<Page> pages, List<String> htmls, String text, String secret_involved, String secret_description, String record_last_modified, String task) {
        this.id = id;
        this.uuid = uuid;
        this.mac = mac;
        this.path = path;
        this.page_count = page_count;
        this.created_time = created_time;
        this.creator = creator;
        this.last_modified_time = last_modified_time;
        this.pages = pages;
        this.htmls = htmls;
        this.text = text;
        this.secret_involved = secret_involved;
        this.secret_description = secret_description;
        this.record_last_modified = record_last_modified;
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getLast_modified_time() {
        return last_modified_time;
    }

    public void setLast_modified_time(String last_modified_time) {
        this.last_modified_time = last_modified_time;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public List<String> getHtmls() {
        return htmls;
    }

    public void setHtmls(List<String> htmls) {
        this.htmls = htmls;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSecret_involved() {
        return secret_involved;
    }

    public void setSecret_involved(String secret_involved) {
        this.secret_involved = secret_involved;
    }

    public String getSecret_description() {
        return secret_description;
    }

    public void setSecret_description(String secret_description) {
        this.secret_description = secret_description;
    }

    public String getRecord_last_modified() {
        return record_last_modified;
    }

    public void setRecord_last_modified(String record_last_modified) {
        this.record_last_modified = record_last_modified;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "id='" + id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", mac='" + mac + '\'' +
                ", path='" + path + '\'' +
                ", page_count=" + page_count +
                ", created_time='" + created_time + '\'' +
                ", last_modified_time='" + last_modified_time + '\'' +
                ", pages=" + pages +
                ", htmls=" + htmls +
                ", text='" + text + '\'' +
                ", secret_involved=" + secret_involved +
                ", secret_description='" + secret_description + '\'' +
                ", record_last_modified='" + record_last_modified + '\'' +
                ", task='" + task + '\'' +
                '}';
    }
}


