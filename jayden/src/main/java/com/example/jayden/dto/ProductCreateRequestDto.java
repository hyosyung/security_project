package com.example.jayden.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequestDto {
    private Long accountId;
    private String name;
    private Long price;
}
