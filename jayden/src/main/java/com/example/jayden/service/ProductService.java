package com.example.jayden.service;

import com.example.jayden.dto.AccountDto;
import com.example.jayden.dto.ProductDto;
import com.example.jayden.dto.Role;
import com.example.jayden.entity.Product;
import com.example.jayden.exception.ProductNotFoundException;
import com.example.jayden.exception.UserInfoValidationException;
import com.example.jayden.repository.ProductRepository;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto createProduct(ProductDto request, AccountDto account) throws UserInfoValidationException {
        if(account == null){
            throw new UserInfoValidationException("로그인 해주세요");
        }
        request.setAccountId(account.getId());
        Product product = productRepository.save(Product.of(request));
        return ProductDto.of(product);
    }

    public List<ProductDto> getAllProducts(){
        return productRepository.findAll().stream().map(ProductDto::of).collect(Collectors.toList());
    }

    public ProductDto getProduct(Long id) throws ProductNotFoundException {
        return ProductDto.of(findProductById(id));
    }

    public ProductDto updateProduct(Long id,ProductDto dto, AccountDto account) throws ProductNotFoundException, UserInfoValidationException {
        Product product = findProductById(id);
        validateUserInfo(product, account);
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        productRepository.save(product);
        return ProductDto.of(product);
    }

    public void deleteProduct(Long id, AccountDto account) throws ProductNotFoundException, UserInfoValidationException {
        Product product = findProductById(id);
        validateUserInfo(product, account);
        productRepository.delete(product);
    }

    private Product findProductById(@NotNull Long id) throws ProductNotFoundException{
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return product.get();
        }
        throw new ProductNotFoundException("해당 상품을 찾을 수 없습니다.");
    }

    private void validateUserInfo(Product product, AccountDto accountDto) throws UserInfoValidationException {
        if(accountDto.getRole()!= Role.ADMIN.name() && product.getAccountId() != accountDto.getId()){
            throw new UserInfoValidationException("해당 상품에 대한 권한이 없습니다.");
        }
    }
}
