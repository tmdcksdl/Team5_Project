package com.example.team5_project.service;

import com.example.team5_project.common.enums.OrderStatus;
import com.example.team5_project.common.exception.MemberException;
import com.example.team5_project.common.exception.OrderException;
import com.example.team5_project.common.exception.ProductException;
import com.example.team5_project.common.exception.StoreException;
import com.example.team5_project.common.exception.errorcode.MemberErrorCode;
import com.example.team5_project.common.exception.errorcode.OrderErrorCode;
import com.example.team5_project.common.exception.errorcode.ProductErrorCode;
import com.example.team5_project.common.exception.errorcode.StoreErrorCode;
import com.example.team5_project.dto.orders.request.CreateOrderRequest;
import com.example.team5_project.dto.orders.request.UpdateOrderRequest;
import com.example.team5_project.dto.orders.response.OrderPageableResponse;
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
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public OrderResponse createOrder(Long memberId, Long productId, CreateOrderRequest requestDto) {
        Member member = memberFindByIdOrThrow(memberId);
        Product product = productFindByIdOrThrow(productId);

        if (product.getStock() == 0) {
            throw new OrderException(OrderErrorCode.OUT_OF_STOCK);
        }

        if (product.getStock() < requestDto.quantity()) {
            throw new OrderException(OrderErrorCode.INSUFFICIENT_STOCK);
        }

        Orders order = new Orders(member, product, OrderStatus.주문접수, requestDto.quantity());
        Orders saveOrder = ordersRepository.save(order);

        return new OrderResponse(saveOrder);
    }

    @Transactional
    public UpdateOrderResponse updateOrderStatus(Long memberId, Long orderId, UpdateOrderRequest requestDto){
        Member member = memberFindByIdOrThrow(memberId);
        Orders order = orderFindByIdOrThrow(orderId);

        Long ownerId = order.getProduct().getStore().getMember().getId();

        if(!Objects.equals(ownerId, memberId)){
            throw new OrderException(OrderErrorCode.UNAUTHORIZED);
        }

        OrderStatus status = OrderStatus.of(requestDto.orderStatus());

        order.updateOrderStatus(status);

        return new UpdateOrderResponse(order);
    }

    @Transactional
    public UpdateOrderResponse cancelOrder(Long memberId, Long orderId){
        Orders order = orderFindByIdOrThrow(orderId);

        Long ordersMemberId = order.getMember().getId();

        if(!Objects.equals(memberId, ordersMemberId)){
            throw new OrderException(OrderErrorCode.UNAUTHORIZED);
        }

        if(!"주문접수".equals(order.getOrderStatus().name())){
            throw new OrderException(OrderErrorCode.CANNOT_CANCEL);
        }

        order.updateOrderStatus(OrderStatus.주문취소);

        return new UpdateOrderResponse(order);
    }


    public Page<OrderPageableResponse> findOrderHistoryByMember(Long memberId, Pageable pageable){

        return ordersRepository.findOrdersByMemberId(memberId, pageable);
    }

    public OrderResponse findOrderDetailByMember(Long memberId, Long orderId){;
        Orders order = orderFindByIdOrThrow(orderId);
        Long ordersMemberId = order.getMember().getId();


        if(!Objects.equals(memberId, ordersMemberId)){
            throw new OrderException(OrderErrorCode.UNAUTHORIZED);
        }

        return new OrderResponse(order);
    }

    public Page<OrderPageableResponse> findAllOrdersForOwner(Long memberId, Long storeId, Pageable pageable){
        Store store = storeFindByIdOrThrow(storeId);
        Long ownerId = store.getMember().getId();

        if(!Objects.equals(memberId, ownerId)){
            throw new OrderException(OrderErrorCode.UNAUTHORIZED);
        }

        Page<OrderPageableResponse> responses =  ordersRepository.findOrdersByStoreId(storeId, pageable);
        return responses;
    }

    public OrderResponse findOrderDetailForOwner(Long memberId, Long storeId, Long orderId){

        Store store = storeFindByIdOrThrow(storeId);
        Long ownerId = store.getMember().getId();

        if(!Objects.equals(memberId, ownerId)){
            throw new OrderException(OrderErrorCode.UNAUTHORIZED);
        }

        Orders order = orderFindByIdOrThrow(orderId);
        return new OrderResponse(order);
    }



    public Member memberFindByIdOrThrow(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    public Store storeFindByIdOrThrow(Long storeId){
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NOT_FOUND_STORE));
    }

    public Orders orderFindByIdOrThrow(Long orderId){
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.NOT_FOUND_ORDER));
    }

    public Product productFindByIdOrThrow(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }
}
