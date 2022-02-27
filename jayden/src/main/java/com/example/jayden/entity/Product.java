package com.example.jayden.entity;

import com.example.jayden.dto.ProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    private String name;

    private Long price;

    public static Product of(ProductDto dto){
        if(dto == null){
            return null;
        }
        Product product = new Product();
        product.setAccountId(dto.getAccountId());
        product.setPrice(dto.getPrice());
        product.setName(dto.getName());
        return product;
    }
}
