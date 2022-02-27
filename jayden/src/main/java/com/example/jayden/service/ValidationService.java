package com.example.jayden.service;

import com.example.jayden.dto.SignInRequestDto;
import com.example.jayden.exception.UserInfoValidationException;

public interface ValidationService {
    void validate(SignInRequestDto request) throws UserInfoValidationException;
}
