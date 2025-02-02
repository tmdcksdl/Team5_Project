package com.example.team5_project.controller;

import com.example.team5_project.dto.product.request.CreateProductRequest;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/{storesId}/product")
    public ResponseEntity<CreateProductResponse> createProduct(
            @RequestBody CreateProductRequest requestDto,
            @PathVariable Long storesId
        ) {

        CreateProductResponse product = productService.createProduct(storesId, requestDto.name(),
                requestDto.price(), requestDto.stock());

        return new ResponseEntity<>(product, HttpStatus.CREATED);

    }
}
