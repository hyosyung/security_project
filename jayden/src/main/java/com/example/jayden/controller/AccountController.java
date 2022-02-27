package com.example.jayden.controller;

import com.example.jayden.dto.AccountDto;
import com.example.jayden.dto.LoginDto;
import com.example.jayden.dto.Response;
import com.example.jayden.dto.SignInRequestDto;
import com.example.jayden.service.AccountService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public Response<AccountDto> createAccount(@RequestBody SignInRequestDto request) {
        return Try.of(() -> Response.ok(accountService.createAccount(request)))
                .onFailure(e -> log.error("{}", e.getMessage(), e))
                .recover(e -> Response.error(e.getMessage()))
                .get();
    }

    @PostMapping("/login")
    public Response<AccountDto> getAccountInfo(@RequestBody LoginDto loginDto) {
        return Try.of(() -> Response.ok(accountService.login(loginDto)))
                .onFailure(e -> log.error("{}", e.getMessage(), e))
                .recover(e -> Response.error(e.getMessage()))
                .get();
    }

    @GetMapping("/me")
    public Response<AccountDto> me(@RequestHeader String token) {
        return Try.of(() -> Response.ok(accountService.getMyInfo(token)))
                .onFailure(e -> log.error("{}", e.getMessage(), e))
                .recover(e -> Response.error(e.getMessage()))
                .get();
    }
}
