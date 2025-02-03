package com.example.team5_project.controller;

import com.example.team5_project.common.aspect.AuthCheck;
import com.example.team5_project.dto.product.request.CreateProductRequest;
import com.example.team5_project.dto.product.request.UpdateProductRequest;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.dto.product.response.PageableProductResponse;
import com.example.team5_project.dto.product.response.ReadProductResponse;
import com.example.team5_project.dto.product.response.UpdateProductResponse;
import com.example.team5_project.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores")
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
    public ResponseEntity<List<ReadProductResponse>> getProducts() {

        return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
    }

    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/{storesId}/products/{productId}")
    public ResponseEntity<ReadProductResponse> getProduct(
            @PathVariable Long productId
    ) {

        return new ResponseEntity<>(productService.getProduct(productId), HttpStatus.OK);
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

    @GetMapping("products")
    public ResponseEntity<Page<PageableProductResponse>> findByPriceRange(
                                                    @RequestParam(name = "minPrice", required = false)Integer minPrice,
                                                    @RequestParam(name = "maxPrice", required = false)Integer maxPrice,
                                                    @RequestParam(name = "page", defaultValue = "1")int page,
                                                    @RequestParam(name = "size", defaultValue = "10")int size
    ){
        Pageable pageable = PageRequest.of(page - 1, size);
       Page<PageableProductResponse> responses = productService.findByPriceRange(pageable, minPrice, maxPrice);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }


}
