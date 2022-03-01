package com.example.jayden.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Role {
    ADMIN,USER;

    public static Map<String, Role> roleMap = Arrays.stream(values()).collect(Collectors.toMap(Enum::name, Function.identity()));

    /**
     *
     * @param role role이 입력되지 않았을 경우 default 권한인 USER 권한을 얻습니다.
     * @return USER, ADMIN 이외의 다른 권한으로 입력이 들어왔을 경우 null을 반환합니다.
     */
    public static Role of(String role){
        if(role == null){
            return USER;
        }
        return roleMap.get(role);
    }

    public static boolean isAdmin(String role){
        return ADMIN.name().equals(role);
    }
}
