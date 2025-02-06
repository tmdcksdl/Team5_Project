package com.example.team5_project.service;

import com.example.team5_project.common.enums.Gender;
import com.example.team5_project.common.enums.OrderStatus;
import com.example.team5_project.common.enums.UserType;
import com.example.team5_project.common.exception.OrderException;
import com.example.team5_project.dto.orders.request.CreateOrderRequest;
import com.example.team5_project.dto.orders.response.OrderResponse;
import com.example.team5_project.dto.orders.response.UpdateOrderResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Orders;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.OrdersRepository;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    Integer price = 50000;


    Member member = new Member(memberId, "이수진", "email@naver.com", "Password13!", Gender.FEMALE, "우리집", UserType.USER);
    Store store = new Store(storeId, "옷가게", member);
    Product product = new Product(productId, "옷", price, 50, store, 10, 100);
    Orders savedOrder = new Orders(member, product, OrderStatus.주문접수, 1);


    @Test
    void 주문_요청() {
        CreateOrderRequest request = new CreateOrderRequest(1);

        ReflectionTestUtils.setField(savedOrder, "id", orderId);

        OrderResponse response = new OrderResponse(orderId, memberId, productId, 50,
                price, OrderStatus.주문접수, now(), now());

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(ordersRepository.save(ArgumentMatchers.any(Orders.class))).thenReturn(savedOrder);

        OrderResponse actualResponse = orderService.createOrder(memberId, productId, request);

        Assertions.assertThat(response.id()).isEqualTo(actualResponse.id());
        Assertions.assertThat(response.productId()).isEqualTo(actualResponse.id());
        Assertions.assertThat(response.totalPrice()).isEqualTo(actualResponse.totalPrice());
    }

    @Test
    void 주문요청_실패_재고부족() {

        CreateOrderRequest request = new CreateOrderRequest(1);

        Product product = new Product(productId, "옷", price, 0, store, 10, 100);

        ReflectionTestUtils.setField(savedOrder, "id", orderId);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(OrderException.class, () -> orderService.createOrder(memberId, productId, request));
    }


    @Test
    void 주문취소_성공(){

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(savedOrder));

        UpdateOrderResponse response = new UpdateOrderResponse(orderId, productId, "주문취소");

        UpdateOrderResponse actualResponse = orderService.cancelOrder(memberId, productId);

        Assertions.assertThat(response.orderStatus()).isEqualTo(actualResponse.orderStatus());
    }

    @Test
    void 주문취소_실패(){
         savedOrder = new Orders(member, product, OrderStatus.배송완료, 1);

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(savedOrder));

        assertThrows(OrderException.class, ()->orderService.cancelOrder(memberId, productId));
    }

    @Test
    void 멤버별_주문건_조회_성공(){

        ReflectionTestUtils.setField(savedOrder, "id", 1L);

        OrderResponse response = new OrderResponse(orderId, memberId, productId, 50,
                price, OrderStatus.주문접수, now(), now());

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(savedOrder));

        OrderResponse actualResponse = orderService.findOrderDetailByMember(memberId, orderId);

        Assertions.assertThat(response.memberId()).isEqualTo(actualResponse.memberId());
        Assertions.assertThat(response.id()).isEqualTo(actualResponse.id());
        Assertions.assertThat(response.orderStatus()).isEqualTo(actualResponse.orderStatus());
    }

    @Test
    void 멤버별_주문건_조회_실패_권한없음(){
        memberId = 2L;

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(savedOrder));

        assertThrows(OrderException.class, ()-> orderService.findOrderDetailByMember(memberId, orderId));
    }
}

