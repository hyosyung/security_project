package com.example.jayden.dto;

import com.example.jayden.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AccountDto {
    private Long id;
    private String username;
    private String role;
    private String token;

    public static AccountDto of(Account account){
        if(account == null){
            return null;
        }
        return AccountDto.builder()
                .role(account.getRole())
                .username(account.getUsername())
                .id(account.getId())
                .build();
    }
}
