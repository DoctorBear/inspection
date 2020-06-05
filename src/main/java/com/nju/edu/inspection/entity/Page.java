package com.nju.edu.inspection.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Page {
    private int pagination;
    private int width;
    private List<Box> boxList;

    public Page() {
    }

    public Page(int pagination, int width, List<Box> boxList) {
        this.pagination = pagination;
        this.width = width;
        this.boxList = boxList;
    }

    public int getPagination() {
        return pagination;
    }

    public void setPagination(int pagination) {
        this.pagination = pagination;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<Box> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<Box> boxList) {
        this.boxList = boxList;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pagination=" + pagination +
                ", width=" + width +
                ", boxList=" + boxList +
                '}';
    }
}
