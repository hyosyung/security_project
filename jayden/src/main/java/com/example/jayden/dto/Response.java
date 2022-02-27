package com.example.jayden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Response<T> {
    T data;
    String errorMessage;

    public static<T> Response<T> ok(T data){
        return new Response<>(data,null);
    }

    public static<T> Response<T> error(String errorMessage){
        return new Response<>(null,errorMessage);
    }
}
