package com.example.jayden.controller;

import com.example.jayden.dto.AccountDto;
import com.example.jayden.dto.ProductDto;
import com.example.jayden.dto.Response;
import com.example.jayden.service.ProductService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public Response<List<ProductDto>> getProducts() {
        return Response.ok(productService.getAllProducts());
    }

    @PostMapping("")
    public Response<ProductDto> createProduct(@RequestBody ProductDto product, @AuthenticationPrincipal AccountDto account) {
        return Try.of(()->Response.ok(productService.createProduct(product, account)))
                .onFailure(e -> log.error("{}",e.getMessage(),e))
                .recover(e -> Response.error(e.getMessage()))
                .get();
    }

    @GetMapping("/{id}")
    public Response<ProductDto> getProduct(@PathVariable Long id) {
        return Try.of(() -> Response.ok(productService.getProduct(id)))
                .onFailure(e -> log.error("{}", e.getMessage(), e))
                .recover(e -> Response.error(e.getMessage()))
                .get();
    }

    @PutMapping("/{id}")
    public Response<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto product, @AuthenticationPrincipal AccountDto account) {
        return Try.of(() -> Response.ok(productService.updateProduct(id, product, account)))
                .onFailure(e -> log.error("{}", e.getMessage(), e))
                .recover(e -> Response.error(e.getMessage()))
                .get();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id, @AuthenticationPrincipal AccountDto account) {
        Try.run(() -> productService.deleteProduct(id, account))
                .onFailure(e -> log.error("{}", e.getMessage(), e));
    }
}
