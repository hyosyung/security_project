package com.example.jayden.entity;

import com.example.jayden.dto.SignInRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseTimeEntity{
    @GeneratedValue(strategy = IDENTITY)
    @JoinColumn(name = "account_id")
    @Id
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String role;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Product> products;

    public static Account of(SignInRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Account account = new Account();
        account.setUsername(dto.getUsername());
        account.setPassword(dto.getPassword());
        account.setRole(dto.getRole());
        return account;
    }
}
