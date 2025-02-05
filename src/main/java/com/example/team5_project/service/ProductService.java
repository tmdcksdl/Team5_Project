package com.example.team5_project.service;

import com.example.team5_project.dto.product.request.UpdateProductRequest;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.dto.product.response.PageableProductResponse;
import com.example.team5_project.dto.product.response.ReadProductResponse;
import com.example.team5_project.dto.product.response.UpdateProductResponse;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.ProductQueryRepository;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ProductQueryRepository productQueryRepository;

    @Transactional
    public CreateProductResponse createProduct(Long storeId, String name, int price, int stock) {

        Store foundStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException());

        if (productRepository.findByName(name).isPresent()){
            throw new RuntimeException();
        }

        Product createdProduct = Product.create(name, price, stock, foundStore);

        Product savedProduct = productRepository.save(createdProduct);

        return new CreateProductResponse(
                savedProduct.getId(), savedProduct.getName(),
                savedProduct.getPrice(), savedProduct.getStock());

    }

    public List<ReadProductResponse> getProducts() {

        List<Product> productList = productRepository.findAll();

        List<ReadProductResponse> responseList = productList.stream()
                .map(product -> new ReadProductResponse(
                        product.getId(), product.getName(),
                        product.getPrice(), product.getStock(), product.getTotalLikes()))
                .collect(Collectors.toList());

        return responseList;

    }

    public ReadProductResponse getProduct(Long productId) {

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException());

        return new ReadProductResponse(
                foundProduct.getId(), foundProduct.getName(),
                foundProduct.getPrice(), foundProduct.getStock(), foundProduct.getTotalLikes());
    }

    @Transactional
    public UpdateProductResponse updateProduct(Long productId, UpdateProductRequest requestDto) {

        Optional<Product> byName = productRepository.findByName(requestDto.name());

        if (byName.isPresent()) {
            throw new RuntimeException();
        }

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException());

        foundProduct.update(requestDto.name(), requestDto.price(), requestDto.stock());

        return new UpdateProductResponse(
                foundProduct.getId(), foundProduct.getName(),
                foundProduct.getPrice(), foundProduct.getStock());
    }

    @Transactional
    public void deleteProduct(Long productId) {

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException());

        productRepository.delete(foundProduct);

    }

    public Page<PageableProductResponse> findByPriceRange(Pageable pageable, Integer minPrice, Integer maxPrice){
        Long startAt = System.currentTimeMillis();
      Page<PageableProductResponse> responses = productQueryRepository.findByPriceRange(minPrice,maxPrice,pageable);
        Long endAt = System.currentTimeMillis();

        log.info("최적화 후 : " + (endAt - startAt) + "ms");

      return responses;
    }
}
