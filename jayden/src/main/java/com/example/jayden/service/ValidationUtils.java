package com.example.jayden.service;

public class ValidationUtils {
    public static boolean isEng(int c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isNum(int c){
        return (c >= '0' && c <= '9');
    }
}
