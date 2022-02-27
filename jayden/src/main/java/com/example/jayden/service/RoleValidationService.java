package com.example.jayden.service;

import com.example.jayden.dto.Role;
import com.example.jayden.dto.SignInRequestDto;
import com.example.jayden.exception.UserInfoValidationException;
import org.springframework.stereotype.Component;

@Component
public class RoleValidationService implements ValidationService{
    @Override
    public void validate(SignInRequestDto request) throws UserInfoValidationException {
        Role role = Role.of(request.getRole());
        if(role == null){
            throw new UserInfoValidationException("ADMIN, USER 이외의 권한으로 등록할 수 없습니다.");
        }
        request.setRole(role.name());
    }
}
