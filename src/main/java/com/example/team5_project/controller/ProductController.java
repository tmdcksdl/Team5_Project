package com.example.team5_project.controller;

import com.example.team5_project.common.aspect.AuthCheck;
import com.example.team5_project.dto.product.request.CreateProductRequest;
import com.example.team5_project.dto.product.request.UpdateProductRequest;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.dto.product.response.PageableProductResponse;
import com.example.team5_project.dto.product.response.ProductResponse;
import com.example.team5_project.dto.product.response.UpdateProductResponse;
import com.example.team5_project.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 등록
     */
    @AuthCheck("OWNER")
    @PostMapping("/{storesId}/products")
    public ResponseEntity<CreateProductResponse> createProduct(
        @RequestBody CreateProductRequest requestDto,
        @PathVariable Long storesId
    ) {
        CreateProductResponse product = productService.createProduct(
                storesId, requestDto.name(),
                requestDto.price(), requestDto.stock()
        );

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * 가게별 상품 전체 조회
     * - id를 기준으로 오름차순 정렬
     * - 한 페이지에 5개의 상품씩 나오도록 설정
     */
    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/{storesId}/products")
    public ResponseEntity<Page<ProductResponse>> getProducts(
        @RequestParam(name = "size",defaultValue = "5")int size,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @PathVariable(name = "storesId") Long storeId,
        HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        String token = extractToken(request);

        Page<ProductResponse> productResponsePage = productService.getProducts(pageable, token, storeId);

        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    /**
     * 상품 단일 조회
     * - 상품 조회 시 해당 상품의 조회수 +1 증가
     */
    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/{storesId}/products/{productId}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long productId,
            HttpServletRequest request
    ) {
        String token = extractToken(request);

        ProductResponse productResponse = productService.getProduct(productId, token);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    /**
     * 상품 조회 (v1)
     * - 검색어가 포함된 상품 조회
     * - 조회수 기준으로 내림차순 정렬
     * - 캐시가 적용되지 않아서 DB에만 검색어 저장
     */
    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/v1/products")
    public ResponseEntity<Page<ProductResponse>> searchByProductName(
        @RequestParam(name = "size",defaultValue = "5")int size,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam String keyword,
        HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        String token = extractToken(request);

        Page<ProductResponse> productResponsePage = productService.searchByProductName(pageable, token, keyword);

        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    /**
     * 상품 조회 (v2)
     * - 검색어가 포함된 상품 조회
     * - 조회수 기준으로 내림차순 정렬
     * - Redis(캐시)가 적용되어서 Redis(캐시)와 DB 둘 다 검색어 저장
     */
    @AuthCheck({"OWNER", "USER"})
    @GetMapping("/v2/products")
    public ResponseEntity<Page<ProductResponse>> searchByProductNameCached(
        @RequestParam(name = "size",defaultValue = "5")int size,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam String keyword,
        HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        String token = extractToken(request);

        Page<ProductResponse> productResponsePage = productService.searchByProductNameCached(pageable, token, keyword);

        return new ResponseEntity<>(productResponsePage, HttpStatus.OK);
    }

    /**
     * 상품 정보 수정
     */
    @AuthCheck({"OWNER"})
    @PatchMapping("/{storesId}/products/{productId}")
    public ResponseEntity<UpdateProductResponse> updateProduct(
        @PathVariable Long productId,
        @RequestBody UpdateProductRequest requestDto
    ){
        UpdateProductResponse productResponse = productService.updateProduct(productId, requestDto);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    /**
     * 상품 삭제
     */
    @AuthCheck({"OWNER"})
    @DeleteMapping("/{storesId}/products/{productId}")
    public ResponseEntity<String> deleteProduct(
        @PathVariable Long productId
    ){
        productService.deleteProduct(productId);

        return new ResponseEntity<>("상품이 삭제 되었습니다.", HttpStatus.NO_CONTENT);
    }

    /**
     * 가격 범위 안에 있는 상품 조회
     */
    @GetMapping("products")
    public ResponseEntity<SliceImpl<PageableProductResponse>> findByPriceRange(
        @RequestParam(name = "minPrice", required = false)Integer minPrice,
        @RequestParam(name = "maxPrice", required = false)Integer maxPrice,
        @RequestParam(name = "page", defaultValue = "1")int page,
        @RequestParam(name = "size", defaultValue = "10")int size
    ){
        Pageable pageable = PageRequest.of(page - 1, size);
        SliceImpl<PageableProductResponse> responses = productService.findByPriceRange(pageable, minPrice, maxPrice);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }


    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        return token;
    }
}
