package com.nju.edu.inspection.utils;

public class SeperatorsUtil {
    public static final char sepa = java.io.File.separator.charAt(0);
    public static String convert(String path){
        char replaced = (sepa == '/' ? '\\' : '/');
        return path.replace(replaced, sepa);
    }

    public static void main(String[] args) {
        System.out.println(SeperatorsUtil.convert("123/\\"));
    }
}
