package com.example.jayden.service;

import com.example.jayden.config.JwtUtil;
import com.example.jayden.dto.AccountDto;
import com.example.jayden.dto.LoginDto;
import com.example.jayden.dto.SignInRequestDto;
import com.example.jayden.entity.Account;
import com.example.jayden.exception.UserInfoValidationException;
import com.example.jayden.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<ValidationService> validationServices;
    private final JwtUtil jwtUtil;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if (Objects.isNull(account)) {
            throw new UsernameNotFoundException("해당 유저 정보를 찾을 수 없습니다.");
        }
        return User.builder()
                .username(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public AccountDto createAccount(SignInRequestDto request) throws UserInfoValidationException {
        for (ValidationService service : validationServices) {
            service.validate(request);
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        if(accountRepository.findAccountByUsername(request.getUsername())!=null){
            throw new UserInfoValidationException("이미 존재하는 아이디입니다.");
        }
        Account account = accountRepository.save(Account.of(request));
        return AccountDto.of(account);
    }

    public AccountDto login(LoginDto loginDto) throws UserInfoValidationException {
        if (Objects.isNull(loginDto.getPassword()) || Objects.isNull(loginDto.getUsername())) {
            log.error("아이디와 비밀번호를 모두 입력해야합니다.");
            throw new UserInfoValidationException("아이디와 비밀번호를 모두 입력해야합니다.");
        }
        Account account = accountRepository.findAccountByUsername(loginDto.getUsername());
        if (Objects.isNull(account)) {
            log.error("해당 유저를 찾을 수 없습니다.");
            throw new UserInfoValidationException("해당 유저를 찾을 수 없습니다.");
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), account.getPassword())) {
            log.error("비밀번호가 틀렸습니다.");
            throw new UserInfoValidationException("비밀번호가 틀렸습니다.");
        }
        UserDetails userDetails = User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();

        AccountDto dto = AccountDto.of(account);
        dto.setToken("Bearer "+jwtUtil.generateToken(userDetails));
        return dto;
    }

    public AccountDto getMyInfo(String token) {
        String jwt, username;
        if (token != null && token.startsWith("Bearer ")) {
            jwt = token.substring(7);
            username = jwtUtil.getUsername(jwt);
        } else {
            log.error("잘못된 토큰 형식입니다.");
            return null;
        }
        AccountDto account = AccountDto.of(accountRepository.findAccountByUsername(username));
        if (account == null) {
            log.error("유저 정보를 찾을 수 없습니다.");
            return null;
        }
        account.setToken(token);
        return account;
    }
}
