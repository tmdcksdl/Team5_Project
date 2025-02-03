package com.example.team5_project.service;

import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.dto.product.request.UpdateProductRequest;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.dto.product.response.OwnerReadProductResponse;
import com.example.team5_project.dto.product.response.ProductResponse;
import com.example.team5_project.dto.product.response.UpdateProductResponse;
import com.example.team5_project.dto.product.response.UserReadProductResponse;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
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
    private final JwtUtil jwtUtil;

    @Transactional
    public CreateProductResponse createProduct(Long storeId, String name, int price, int stock) {

        Store foundStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException());

        if (productRepository.findByName(name).isPresent()) {
            throw new RuntimeException();
        }

        Product createdProduct = Product.create(name, price, stock, foundStore);

        Product savedProduct = productRepository.save(createdProduct);

        return new CreateProductResponse(
                savedProduct.getId(), savedProduct.getName(),
                savedProduct.getPrice(), savedProduct.getStock());

    }

    public List<? extends ProductResponse> getProducts(String token) {

        String userType = jwtUtil.extractUserType(token);

        List<Product> productList = productRepository.findAll();

        if (userType.equalsIgnoreCase("USER")) {
            return productList.stream()
                    .map(product -> new UserReadProductResponse(
                            product.getId(), product.getName(),
                            product.getPrice(), product.getTotalLikes()))
                    .collect(Collectors.toList());
        }

        if (userType.equalsIgnoreCase("OWNER")) {
            return productList.stream()
                    .map(product -> new OwnerReadProductResponse(
                            product.getId(), product.getName(),
                            product.getPrice(), product.getStock(), product.getTotalLikes()))
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("유효하지 않은 사용자 유형입니다.");
    }

    public ProductResponse getProduct(Long productId, String token) {

        String userType = jwtUtil.extractUserType(token);

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException());

        if (userType.equalsIgnoreCase("USER")) {
            return new UserReadProductResponse(
                    foundProduct.getId(), foundProduct.getName(),
                    foundProduct.getPrice(), foundProduct.getTotalLikes());
        }

        if (userType.equalsIgnoreCase("OWNER")) {
            return new OwnerReadProductResponse(
                    foundProduct.getId(), foundProduct.getName(),
                    foundProduct.getPrice(), foundProduct.getStock(), foundProduct.getTotalLikes());
        }

        throw new IllegalArgumentException("유효하지 않은 사용자 유형입니다.");
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
}
