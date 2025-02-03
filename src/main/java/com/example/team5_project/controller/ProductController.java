package com.example.team5_project.controller;

import com.example.team5_project.common.aspect.AuthCheck;
import com.example.team5_project.dto.product.request.CreateProductRequest;
import com.example.team5_project.dto.product.request.UpdateProductRequest;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.dto.product.response.ReadProductResponse;
import com.example.team5_project.dto.product.response.UpdateProductResponse;
import com.example.team5_project.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @AuthCheck({"OWNER"})
    @PostMapping("/{storesId}/products")
    public ResponseEntity<CreateProductResponse> createProduct(
            @RequestBody CreateProductRequest requestDto,
            @PathVariable Long storesId
        ) {

        CreateProductResponse product = productService.createProduct(storesId, requestDto.name(),
                requestDto.price(), requestDto.stock());

        return new ResponseEntity<>(product, HttpStatus.CREATED);

    }

    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/{storesId}/products")
    public ResponseEntity<List<? extends ReadProductResponse>> getProducts(HttpServletRequest request) {

        String token = extractToken(request);
        return new ResponseEntity<>(productService.getProducts(token), HttpStatus.OK);
    }

    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/{storesId}/products/{productId}")
    public ResponseEntity<? extends ReadProductResponse> getProduct(
            @PathVariable Long productId,
            HttpServletRequest request
    ) {

        String token = extractToken(request);
        return new ResponseEntity<>(productService.getProduct(productId, token), HttpStatus.OK);
    }

    @AuthCheck({"OWNER"})
    @PatchMapping("/{storesId}/products/{productId}")
    public ResponseEntity<UpdateProductResponse> updateProduct(
            @PathVariable Long productId,
            @RequestBody UpdateProductRequest requestDto
    ){

        return new ResponseEntity<>(productService.updateProduct(productId, requestDto), HttpStatus.OK);

    }

    @AuthCheck({"OWNER"})
    @DeleteMapping("/{storesId}/products/{productId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long productId
    ){

        productService.deleteProduct(productId);

        return new ResponseEntity<>("상품이 삭제 되었습니다.", HttpStatus.NO_CONTENT);
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return token;
    }

}
