package com.example.jayden.service;

import com.example.jayden.dto.SignInRequestDto;
import com.example.jayden.exception.UserInfoValidationException;
import com.example.jayden.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernameValidationService implements ValidationService {

    private final AccountRepository repository;

    @Override
    public void validate(SignInRequestDto request) throws UserInfoValidationException {
        String username = request.getUsername();
        if (username == null || username.isEmpty()) {
            throw new UserInfoValidationException("아이디를 입력해주세요.");
        }
        if (username.length() < 3 || username.length() > 20) {
            throw new UserInfoValidationException("아이디는 3자이상 20자 이하여야 합니다.");
        }
        if (isInvalid(username)) {
            throw new UserInfoValidationException("아이디에는 영어 대/소문자, 숫자, _(underscore),-(dash)만 입력할 수 있습니다.");
        }
        if(repository.existsAccountByUsername(username)){
            throw new UserInfoValidationException("이미 존재하는 아이디입니다.");
        }
    }

    private boolean isInvalid(String username) {
        return username.chars().anyMatch(this::isInvalidCharacter);
    }

    private boolean isInvalidCharacter(int c) {
        return !(c == '-' || c == '_' || ValidationUtils.isEng(c) || ValidationUtils.isNum(c));
    }
}
