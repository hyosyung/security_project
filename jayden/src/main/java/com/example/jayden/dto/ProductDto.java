package com.example.jayden.dto;

import com.example.jayden.entity.Product;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDto {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Long price;
    private Long accountId;

    public static ProductDto of(Product product){
        if(product == null){
            return null;
        }
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .accountId(product.getAccountId())
                .build();
    }
}
