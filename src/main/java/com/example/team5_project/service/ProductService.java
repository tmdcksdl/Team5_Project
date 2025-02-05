package com.example.team5_project.service;

import com.example.team5_project.common.exception.MemberException;
import com.example.team5_project.common.exception.ProductException;
import com.example.team5_project.common.exception.StoreException;
import com.example.team5_project.common.exception.errorcode.MemberErrorCode;
import com.example.team5_project.common.exception.errorcode.ProductErrorCode;
import com.example.team5_project.common.exception.errorcode.StoreErrorCode;
import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.dto.product.request.UpdateProductRequest;
import com.example.team5_project.dto.product.response.*;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Search;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.SearchRepository;
import com.example.team5_project.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final SearchRepository searchRepository;
    private final CacheService cacheService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    @Transactional
    public CreateProductResponse createProduct(Long storeId, String name, int price, int stock) {

        Store foundStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND_STORE));

        if (productRepository.findByName(name).isPresent()) {
            throw new ProductException(ProductErrorCode.ALREADY_EXIST_PRODUCT);
        }

        Product createdProduct = Product.create(name, price, stock, foundStore);

        Product savedProduct = productRepository.save(createdProduct);

        return new CreateProductResponse(
                savedProduct.getId(), savedProduct.getName(),
                savedProduct.getPrice(), savedProduct.getStock());

    }

    /**
     * store_id 에 해당하는 상품 조회
     * @param pageable
     * @param token
     * @param storeId
     * @return
     */
    public Page<? extends ProductResponse> getProducts(Pageable pageable, String token, Long storeId) {

        String userType = jwtUtil.extractUserType(token);

        if (!storeRepository.existsById(storeId)){
            throw new StoreException(StoreErrorCode.NOT_FOUND_STORE);
        }

        Page<Product> productPage = productRepository.findProductByStoreId(storeId, pageable);

        if (userType.equalsIgnoreCase("USER")) {
            return productPage
                    .map(product -> new UserReadProductResponse(
                            product.getId(), product.getName(),
                            product.getPrice(), product.getTotalLikes(), product.getTotalViewCounts()));
        }

        if (userType.equalsIgnoreCase("OWNER")) {
            return productPage
                    .map(product -> new OwnerReadProductResponse(
                            product.getId(), product.getName(),
                            product.getPrice(), product.getStock(),
                            product.getTotalLikes(), product.getTotalViewCounts()));
        }
        throw new MemberException(MemberErrorCode.INVALID_USER_TYPE);
    }

    /**
     * 상품 단건 조회 - store_id 로 조회
     * @param productId
     * @param token
     * @return
     */
    @Transactional
    public ProductResponse getProduct(Long productId, String token) {

        String userType = jwtUtil.extractUserType(token);

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT));

        foundProduct.addViewCount();

        if (userType.equalsIgnoreCase("USER")) {
            return new UserReadProductResponse(
                    foundProduct.getId(), foundProduct.getName(),
                    foundProduct.getPrice(), foundProduct.getTotalLikes(), foundProduct.getTotalViewCounts());
        }

        if (userType.equalsIgnoreCase("OWNER")) {
            return new OwnerReadProductResponse(
                    foundProduct.getId(), foundProduct.getName(),
                    foundProduct.getPrice(), foundProduct.getStock(),
                    foundProduct.getTotalLikes(), foundProduct.getTotalViewCounts());
        }

        throw new MemberException(MemberErrorCode.INVALID_USER_TYPE);
    }

    /**
     * 검색어로 조회시 해당하는 상품 전체 조회 -> search 객체 생성
     * @param pageable
     * @param token
     * @param keyword
     * @return
     */
    //
    @Transactional
    public Page<? extends ProductResponse> searchByProductName(Pageable pageable, String token, String keyword) {

        String userType = jwtUtil.extractUserType(token);

        Search search = searchRepository.findByName(keyword).orElse(null);

        // 검색어를 DB에도 저장 (중복 방지)
        if (search != null) {
            search.incrementCount();
        } else {
            Search createdSearch = Search.of(keyword);
            searchRepository.save(createdSearch);
        }

        Page<Product> productPage = productRepository.searchByName(keyword, pageable);

        if (userType.equalsIgnoreCase("USER")) {
            return productPage
                    .map(product -> new UserReadProductResponse(
                            product.getId(), product.getName(),
                            product.getPrice(), product.getTotalLikes(), product.getTotalViewCounts()));
        }

        if (userType.equalsIgnoreCase("OWNER")) {
            return productPage
                    .map(product -> new OwnerReadProductResponse(
                            product.getId(), product.getName(),
                            product.getPrice(), product.getStock(),
                            product.getTotalLikes(), product.getTotalViewCounts()));
        }
        throw new MemberException(MemberErrorCode.INVALID_USER_TYPE);
    }

    @Transactional
    @Cacheable(value = "searchProducts", key = "#keyword")  // Local memory Cache 적용 -> Redis Cache 적용
    public Page<? extends ProductResponse> searchByProductNameCached(Pageable pageable, String token, String keyword) {

        String userType = jwtUtil.extractUserType(token);

        // 검색어를 캐시에 저장 (+ 횟수 증가)
        if (!(keyword == null || keyword.trim().isEmpty())) {
//            cacheService.saveKeywordToCache(keyword);
            redisService.saveKeywordToCache(keyword);
        }

        Search search = searchRepository.findByName(keyword).orElse(null);

        // 검색어를 DB에도 저장 (중복 방지)
        if (search != null) {
            search.incrementCount();
        } else {
            Search createdSearch = Search.of(keyword);
            searchRepository.save(createdSearch);
        }

        Page<Product> productPage = productRepository.searchByName(keyword, pageable);

        if (userType.equalsIgnoreCase("USER")) {
            return productPage
                    .map(product -> new UserReadProductResponse(
                            product.getId(), product.getName(),
                            product.getPrice(), product.getTotalLikes(), product.getTotalViewCounts()));
        }

        if (userType.equalsIgnoreCase("OWNER")) {
            return productPage
                    .map(product -> new OwnerReadProductResponse(
                            product.getId(), product.getName(),
                            product.getPrice(), product.getStock(),
                            product.getTotalLikes(), product.getTotalViewCounts()));
        }
        throw new MemberException(MemberErrorCode.INVALID_USER_TYPE);
    }

    @Transactional
    public UpdateProductResponse updateProduct(Long productId, UpdateProductRequest requestDto) {

        if (productRepository.existsById(productId)) {
            throw new ProductException(ProductErrorCode.ALREADY_EXIST_PRODUCT);
        }

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT));

        foundProduct.update(requestDto.name(), requestDto.price(), requestDto.stock());

        return new UpdateProductResponse(
                foundProduct.getId(), foundProduct.getName(),
                foundProduct.getPrice(), foundProduct.getStock());
    }

    @Transactional
    public void deleteProduct(Long productId) {

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT));

        productRepository.delete(foundProduct);

    }
}
