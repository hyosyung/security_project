package com.example.jayden.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().disable() // Http basic Auth기반 로그인. disable시 인증X.
                .csrf().disable()  // rest api -> csrf 보안이 필요X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // jwt token인증 -> stateless 하도록 처리.
                .and()
                .authorizeRequests()
                .antMatchers("/accounts/**").permitAll()
                .antMatchers("/products**").permitAll()  // 인증권한이 필요한 페이지.
                .antMatchers("/products/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
