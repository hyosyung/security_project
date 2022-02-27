package com.example.jayden.service;

import com.example.jayden.dto.SignInRequestDto;
import com.example.jayden.exception.UserInfoValidationException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidationService implements ValidationService {

    @Override
    public void validate(SignInRequestDto request) throws UserInfoValidationException {
        String password = request.getPassword();
        if (password == null || password.isEmpty()) {
            throw new UserInfoValidationException("비밀번호를 입력해주세요.");
        }
        if (password.length() < 8) {
            throw new UserInfoValidationException("최소 8자의 비밀번호를 입력해주세요");
        }
        if (isSatisfiedCondition(password)) {
            return;
        }
        throw new UserInfoValidationException("영문자 + 숫자 + 특수문자의 조합으로 비밀번호를 설정해주세요.");
    }

    private boolean isSatisfiedCondition(String password) {
        int eng = 0, num = 0, other = 0;
        int[] passwordChars = password.chars().toArray();
        for (int c : passwordChars) {
            if (ValidationUtils.isNum(c)) {
                num++;
            }
            if (ValidationUtils.isEng(c)) {
                eng++;
            } else {
                other++;
            }
        }
        return eng != 0 && num != 0 && other != 0;
    }
}
