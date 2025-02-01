package com.example.team5_project.service;

import com.example.team5_project.common.enums.OrderStatus;
import com.example.team5_project.common.exception.OrderCancellationException;
import com.example.team5_project.common.exception.OutOfStockException;
import com.example.team5_project.dto.orders.request.CreateOrderRequest;
import com.example.team5_project.dto.orders.request.UpdateOrderRequest;
import com.example.team5_project.dto.orders.response.OrderResponse;
import com.example.team5_project.dto.orders.response.OrderPageableResponse;
import com.example.team5_project.dto.orders.response.UpdateOrderResponse;
import com.example.team5_project.entity.Member;
import com.example.team5_project.entity.Orders;
import com.example.team5_project.entity.Product;
import com.example.team5_project.entity.Store;
import com.example.team5_project.repository.MemberRepository;
import com.example.team5_project.repository.OrdersRepository;
import com.example.team5_project.repository.ProductRepository;
import com.example.team5_project.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final StoreRepository storeRepository;

    @Transactional
    public OrderResponse createOrder(Long memberId, Long productId, CreateOrderRequest requestDto) {

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

        return new OrderResponse(saveOrder);
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


    @Transactional
    public UpdateOrderResponse cancelOrder(Long memberId, Long orderId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NullPointerException("해당 멤버는 존재하지 않습니다"));

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(()->new NullPointerException("해당 주문건은 존재하지 않습니다"));

        if(member.equals(order.getMember())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인의 주문만 상태변경할 수 있습니다");
        }

        if(!"주문접수".equals(order.getOrderStatus().name())){
            throw new OrderCancellationException("주문취소가 불가능합니다");
        }

        order.updateOrderStatus(OrderStatus.주문취소);

        return new UpdateOrderResponse(order);

    }


    public Page<OrderPageableResponse> findOrderHistoryByMember(Long memberId, Pageable pageable){
        Page<OrderPageableResponse> responses =  ordersRepository.findOrdersByMemberId(memberId, pageable);
        return responses;
    }

    public OrderResponse findOrderDetailByMember(Long memberId, Long orderId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NullPointerException("해당 멤버는 존재하지 않습니다"));

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(()->new NullPointerException("해당 주문건은 존재하지 않습니다"));

        if(member.equals(order.getMember())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인의 주문만 확인할 수 있습니다");
        }

        return new OrderResponse(order);
    }

    public Page<OrderPageableResponse> findAllOrdersForOwner(Long storeId, Pageable pageable){
        Page<OrderPageableResponse> responses =  ordersRepository.findOrdersByStoreId(storeId, pageable);
        return responses;
    }

    public OrderResponse findOrderDetailForOwner(Long memberId, Long storeId, Long orderId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NullPointerException("해당 멤버는 존재하지 않습니다"));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NullPointerException("해당 가게는 존재하지 않습니다"));

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(()->new NullPointerException("해당 주문건은 존재하지 않습니다"));

        if(!member.equals(store.getMember())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인 가게의 주문만 확인할 수 있습니다");
        }

        return new OrderResponse(order);
    }


}
