package com.example.team5_project.service;

import com.example.team5_project.common.enums.OrderStatus;
import com.example.team5_project.dto.orders.request.CreateOrderRequest;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Orders;
import com.example.team5_project.entity.Product;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.OrdersRepository;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrdersRepository ordersRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    StoreRepository storeRepository;



    Long memberId = 1L;
    Long storeId = 1L;
    Long productId = 1L;
    Long orderId = 1L;

    Member member = new Member();
    Product product = new Product();




    @Test
    void 주문_요청(){
        CreateOrderRequest request = new CreateOrderRequest(50);
        Orders savedOrder = new Orders(member, product, OrderStatus.주문접수, request.quantity());





        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));





    }

}