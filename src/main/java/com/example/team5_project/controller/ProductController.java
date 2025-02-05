package com.example.team5_project.controller;

import com.example.team5_project.common.aspect.AuthCheck;
import com.example.team5_project.dto.product.request.CreateProductRequest;
import com.example.team5_project.dto.product.request.UpdateProductRequest;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.dto.product.response.ProductResponse;
import com.example.team5_project.dto.product.response.PageableProductResponse;
import com.example.team5_project.dto.product.response.ReadProductResponse;
import com.example.team5_project.dto.product.response.UpdateProductResponse;
import com.example.team5_project.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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

    /**
     * store_id 에 해당하는 상품 조회
     * @param size
     * @param page
     * @param storeId
     * @param request
     * @return
     */
    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/{storesId}/products")
    public ResponseEntity<Page<? extends ProductResponse>> getProducts(
            @RequestParam(name = "size",defaultValue = "5")int size,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @PathVariable(name = "storesId") Long storeId,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);

        String token = extractToken(request);
        return new ResponseEntity<>(productService.getProducts(pageable, token, storeId), HttpStatus.OK);
    }

    /**
     * 검색어로 조회시 해당하는 상품 전체 조회 -> search 객체 생성
     * @param size
     * @param page
     * @param request
     * @param keyword
     * @return
     */
    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/v1/products")
    public ResponseEntity<Page<? extends ProductResponse>> searchByProductName(
            @RequestParam(name = "size",defaultValue = "5")int size,
            @RequestParam(name = "page", defaultValue = "1") int page,
            HttpServletRequest request,
            @RequestParam String keyword
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        String token = extractToken(request);
        return new ResponseEntity<>(productService.searchByProductName(pageable, token, keyword), HttpStatus.OK);
    }

    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/v2/products")
    public ResponseEntity<Page<? extends ProductResponse>> searchByProductNameCached(
            @RequestParam(name = "size",defaultValue = "5")int size,
            @RequestParam(name = "page", defaultValue = "1") int page,
            HttpServletRequest request,
            @RequestParam String keyword) {

        Pageable pageable = PageRequest.of(page - 1, size);

        String token = extractToken(request);
        return new ResponseEntity<>(productService.searchByProductNameCached(pageable, token, keyword), HttpStatus.OK);
    }

    /**
     * product_id 로 조회 -> 조회수 증가
     * @param productId
     * @param request
     * @return
     */
    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/{storesId}/products/{productId}")
    public ResponseEntity<? extends ProductResponse> getProduct(
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


    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return token;
    }

}
