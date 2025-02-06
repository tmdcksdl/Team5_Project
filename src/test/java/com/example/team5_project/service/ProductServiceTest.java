package com.example.team5_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.team5_project.common.exception.MemberException;
import com.example.team5_project.common.exception.ProductException;
import com.example.team5_project.common.exception.StoreException;
import com.example.team5_project.common.utils.JwtUtil;
import com.example.team5_project.dto.product.request.UpdateProductRequest;
import com.example.team5_project.dto.product.response.CreateProductResponse;
import com.example.team5_project.dto.product.response.OwnerReadProductResponse;
import com.example.team5_project.dto.product.response.ProductResponse;
import com.example.team5_project.dto.product.response.UpdateProductResponse;
import com.example.team5_project.dto.product.response.UserReadProductResponse;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.ProductQueryRepository;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.SearchRepository;
import com.example.team5_project.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    Store store = mock(Store.class);
    Product product = Product.create("name", 1000, 50, store);

    @Test
    public void 상품_생성() throws Exception {

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

        assertThrows(StoreException.class,
                () -> productService.createProduct(storeId, "name", 1000, 50));
        
    }
    
    @Test
    public void USER_TYPE_으로_조회() throws Exception {
        Long storeId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        String token = "token";

        // given
        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(jwtUtil.extractUserType(token)).thenReturn("USER");

        List<Product> products = List.of(
                Product.create("Product1", 1000, 10, new Store()),
                Product.create("Product2", 1000, 5, new Store())
        );

        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
        when(productRepository.findProductByStoreId(storeId, pageable)).thenReturn(productPage);

        // when
        Page<ProductResponse> result = productService.getProducts(pageable, token, storeId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getContent().get(0)).isInstanceOf(UserReadProductResponse.class);
        assertThat(result.getContent().get(0).name()).isEqualTo("Product1");
    }

    @Test
    public void OWNER_TYPE_으로_조회() throws Exception {
        Long storeId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        String token = "token";

        // given
        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(jwtUtil.extractUserType(token)).thenReturn("OWNER");

        List<Product> products = List.of(
                Product.create("Product1", 1000, 10, new Store()),
                Product.create("Product2", 1000, 5, new Store())
        );

        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
        when(productRepository.findProductByStoreId(storeId, pageable)).thenReturn(productPage);
        // when
        Page<ProductResponse> result = productService.getProducts(pageable, token, storeId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getContent().get(0)).isInstanceOf(OwnerReadProductResponse.class);
        assertThat(result.getContent().get(0).name()).isEqualTo("Product1");
    }
    @Test
    public void 올바르지_않은_유저_타입_발생() throws Exception {

        Long storeId = 1L;
        String token = "test-token";
        Pageable pageable = PageRequest.of(0,10);

        // given
        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(jwtUtil.extractUserType(token)).thenReturn("INVALID_TYPE");

        assertThrows(MemberException.class, () -> productService.getProducts(pageable,token, storeId));
    }

    @Test
    public void 상품_수정() throws Exception {

        UpdateProductRequest request = new UpdateProductRequest("UpdatedProduct", 1500, 1000);

        // given
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        // when
        UpdateProductResponse response = productService.updateProduct(product.getId(),
                request);
        // then
        assertThat(response.name()).isEqualTo("UpdatedProduct");
        assertThat(response.price()).isEqualTo(1500);
        assertThat(response.stock()).isEqualTo(1000);
    }

    @Test
    public void 존재하지_않는_상품_조회_예외발생() throws Exception {
        // given
        Long productId = 100000L;
        UpdateProductRequest request = mock(UpdateProductRequest.class);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.updateProduct(productId, request));
    }

    @Test
    public void 상품_삭제_성공() throws Exception {


        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // given
        doNothing().when(productRepository).delete(product);

        // when
        productService.deleteProduct(product.getId());

        // then
        verify(productRepository, times(1)).delete(product);
    }

}