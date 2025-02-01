package com.example.team5_project.service;

import com.example.team5_project.common.enums.OrderStatus;
import com.example.team5_project.common.exception.OutOfStockException;
import com.example.team5_project.dto.orders.request.CreateOrderRequest;
import com.example.team5_project.dto.orders.request.UpdateOrderRequest;
import com.example.team5_project.dto.orders.response.CreateOrderResponse;
import com.example.team5_project.dto.orders.response.OrderPageableResponse;
import com.example.team5_project.dto.orders.response.ReadOrderResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Orders;
import com.example.team5_project.entity.Product;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.OrdersRepository;
import com.example.team5_project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public CreateOrderResponse createOrder(Long memberId, Long productId, CreateOrderRequest requestDto) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NullPointerException("해당 멤버는 존재하지 않습니다"));

        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NullPointerException("해당 상품은 존재하지 않습니다"));


        if (findProduct.getStock() == 0) {
            throw new OutOfStockException("해당 제품은 품절입니다");
        }

        if (findProduct.getStock() < requestDto.quantity()) {
            throw new IndexOutOfBoundsException(
                    "해당 제품의 재고가 선택하신 수량보다 부족합니다. 재고:" + findProduct.getStock()
            );
        }

        Orders order = new Orders(findMember, findProduct, OrderStatus.주문접수, requestDto.quantity());
        Orders saveOrder = ordersRepository.save(order);

        return new CreateOrderResponse(saveOrder);
    }

    public UpdateOrderRequest updateOrderStatus(){
        return null;
    }

    public OrderPageableResponse findOrderHistoryByMember(){
        return null;
    }

    public ReadOrderResponse findOrderDetailByMember(){
        return null;
    }

    public OrderPageableResponse findAllOrdersForOwner(){
        return null;
    }

    public ReadOrderResponse findOrderDetailForOwner(){
        return null;
    }


}
