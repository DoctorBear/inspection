package com.nju.edu.inspection.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Box {

    private int line_height;
    private int num;
    private List<Integer> box;
    private String text;

    public Box(int line_height, int num, List<Integer> box, String text) {
        this.line_height = line_height;
        this.num = num;
        this.box = box;
        this.text = text;
    }

    public Box() {
    }

    @Override
    public String toString() {
        return "Box{" +
                "line_height=" + line_height +
                ", num=" + num +
                ", box=" + box +
                ", text='" + text + '\'' +
                '}';
    }

    public int getLine_height() {
        return line_height;
    }

    public void setLine_height(int line_height) {
        this.line_height = line_height;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Integer> getBox() {
        return box;
    }

    public void setBox(List<Integer> box) {
        this.box = box;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
