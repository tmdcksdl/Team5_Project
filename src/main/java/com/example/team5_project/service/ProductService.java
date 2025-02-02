package com.example.team5_project.service;

import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public CreateProductResponse createProduct(Long storeId, String name, int price, int stock) {

        Store foundStore = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException());

        Product createdProduct = Product.create(name, price, stock, foundStore);

        Product savedProduct = productRepository.save(createdProduct);

        return new CreateProductResponse(savedProduct.getName(), savedProduct.getPrice(), savedProduct.getStock());

    }
}
