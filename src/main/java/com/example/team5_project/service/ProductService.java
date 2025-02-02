package com.example.team5_project.service;

import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.dto.product.response.ReadProductResponse;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<ReadProductResponse> getProduct() {

        List<Product> productList = productRepository.findAll();

        List<ReadProductResponse> responseList = productList.stream()
                .map(product -> new ReadProductResponse(
                        product.getId(), product.getName(),
                        product.getPrice(), product.getStock(), product.getTotalLikes()))
                .collect(Collectors.toList());

        return responseList;

    }
}
