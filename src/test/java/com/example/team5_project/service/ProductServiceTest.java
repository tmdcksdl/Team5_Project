package com.example.team5_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.team5_project.common.exception.StoreException;
import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.ProductQueryRepository;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.SearchRepository;
import com.example.team5_project.repository.StoreRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    StoreRepository storeRepository;

    @Mock
    SearchRepository searchRepository;

    @Mock
    RedisService redisService;

    @Mock
    ProductQueryRepository productQueryRepository;

    @Mock
    JwtUtil jwtUtil;

    @Test
    public void 상품_생성() throws Exception {

        Store store = mock(Store.class);
        Product product = Product.create("name", 1000, 50, store);

        // given
        when(store.getId()).thenReturn(1L);
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // when
        CreateProductResponse actualResponse = productService.createProduct(store.getId(), "name",
                1000,
                50);

        // then
        assertThat(actualResponse.product_id()).isEqualTo(product.getId());
        assertThat(actualResponse.name()).isEqualTo(product.getName());
    }

    @Test
    public void 존재하지_않는_가게로_상품_생성() throws Exception {
        // given
        Long storeId = 10000000L;
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        Assertions.assertThrows(StoreException.class,
                () -> productService.createProduct(storeId, "name", 1000, 50));


    }


}