package com.example.team5_project.controller;

import com.example.team5_project.dto.orders.request.CreateOrderRequest;
import com.example.team5_project.dto.orders.request.UpdateOrderRequest;
import com.example.team5_project.dto.orders.response.CreateOrderResponse;
import com.example.team5_project.dto.orders.response.OrderPageableResponse;
import com.example.team5_project.dto.orders.response.ReadOrderResponse;
import com.example.team5_project.dto.orders.response.UpdateOrderResponse;
import com.example.team5_project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @PostMapping("/product/{productId}/order")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestAttribute("id") Long memberId,
                                                          @PathVariable(name = "productId")Long productId,
                                                          @RequestBody CreateOrderRequest requestDto
    ){
        CreateOrderResponse response = orderService.createOrder(memberId, productId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/order/{ordersId}")
    public ResponseEntity<UpdateOrderResponse> updateOrderStatus(@RequestAttribute("id")Long memberId,
                                                                 @PathVariable(name ="orderId")Long orderId,
                                                                 @RequestBody UpdateOrderRequest requestDto
    ){
        UpdateOrderResponse response = orderService.updateOrderStatus(memberId, orderId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @GetMapping("/orders")
    public ResponseEntity<Page<OrderPageableResponse>> findOrderHistoryByMember(@RequestAttribute("id")Long MemberId){
        return null;
    }

    @GetMapping("/order/{ordersId}")
    public ResponseEntity<ReadOrderResponse> findOrderDetailByMember(@RequestAttribute("id")Long MemberId,
                                                                     @PathVariable(name ="orderId")Long orderId
    ){
        return null;
    }

    @GetMapping("/store/{storeId}/orders")
    public ResponseEntity<Page<OrderPageableResponse>> findAllOrdersForOwner (@RequestAttribute("id")Long MemberId){
        return null;
    }

    @GetMapping("/store/{storeId}/order/{orderId}")
    public ResponseEntity<ReadOrderResponse> findOrderDetailForOwner(@RequestAttribute("id")Long MemberId,
                                                                     @PathVariable(name ="orderId")Long orderId
    ){
        return null;
    }

}
