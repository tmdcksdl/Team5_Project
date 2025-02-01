package com.example.team5_project.service;

import com.example.team5_project.common.enums.OrderStatus;
import com.example.team5_project.common.exception.OrderCancellationException;
import com.example.team5_project.common.exception.OutOfStockException;
import com.example.team5_project.dto.orders.request.CreateOrderRequest;
import com.example.team5_project.dto.orders.request.UpdateOrderRequest;
import com.example.team5_project.dto.orders.response.CreateOrderResponse;
import com.example.team5_project.dto.orders.response.OrderPageableResponse;
import com.example.team5_project.dto.orders.response.ReadOrderResponse;
import com.example.team5_project.dto.orders.response.UpdateOrderResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Orders;
import com.example.team5_project.entity.Product;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.OrdersRepository;
import com.example.team5_project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
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


    @Transactional
    public UpdateOrderResponse updateOrderStatus(Long memberId, Long orderId, UpdateOrderRequest requestDto){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NullPointerException("해당 멤버는 존재하지 않습니다"));

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(()->new NullPointerException("해당 주문건은 존재하지 않습니다"));

        if(!order.getMember().equals(member)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인 가게에대한 주문건만 상태변경할 수 있습니다");
        }

        OrderStatus status = OrderStatus.of(requestDto.orderStatus());

        order.updateOrderStatus(status);

        return new UpdateOrderResponse(order);
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
