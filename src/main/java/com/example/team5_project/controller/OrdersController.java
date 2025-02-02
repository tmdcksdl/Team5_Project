package com.example.team5_project.controller;

import com.example.team5_project.dto.orders.request.CreateOrderRequest;
import com.example.team5_project.dto.orders.request.UpdateOrderRequest;
import com.example.team5_project.dto.orders.response.OrderResponse;
import com.example.team5_project.dto.orders.response.OrderPageableResponse;
import com.example.team5_project.dto.orders.response.UpdateOrderResponse;
import com.example.team5_project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @PostMapping("/product/{productId}/order")
    public ResponseEntity<OrderResponse> createOrder(@RequestAttribute("id") Long memberId,
                                                     @PathVariable(name = "productId")Long productId,
                                                     @RequestBody CreateOrderRequest requestDto
    ){
        OrderResponse response = orderService.createOrder(memberId, productId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/order/{orderId}")
    public ResponseEntity<UpdateOrderResponse> updateOrderStatus(@RequestAttribute("id")Long memberId,
                                                                 @PathVariable(name ="orderId")Long orderId,
                                                                 @RequestBody UpdateOrderRequest requestDto
    ){
        UpdateOrderResponse response = orderService.updateOrderStatus(memberId, orderId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<UpdateOrderResponse> cancelOrder(//@RequestAttribute("id")Long memberId,
                                            @PathVariable(name ="orderId")Long orderId
    ){
        UpdateOrderResponse response = orderService.cancelOrder(1L, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<OrderPageableResponse>> findOrderHistoryByMember(@RequestAttribute("id")Long memberId,
                                                                                @RequestParam(name = "size",defaultValue = "5")int size,
                                                                                @RequestParam(name = "page", defaultValue = "1")int page
    ){
        Pageable pageable = PageRequest.of(page-1, size);
        Page<OrderPageableResponse> response = orderService.findOrderHistoryByMember(memberId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> findOrderDetailByMember(@RequestAttribute("id")Long memberId,
                                                                     @PathVariable(name ="orderId")Long orderId
    ){
        OrderResponse response = orderService.findOrderDetailByMember(memberId, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/store/{storeId}/orders")
    public ResponseEntity<Page<OrderPageableResponse>> findAllOrdersForOwner (@RequestAttribute("id")Long memberId,
                                                                              @RequestParam(name = "size",defaultValue = "5")int size,
                                                                              @RequestParam(name = "page", defaultValue = "1")int page,
                                                                              @PathVariable(name = "storeId") Long storeId
    ){
        Pageable pageable = PageRequest.of(page-1, size);
        Page<OrderPageableResponse> response = orderService.findAllOrdersForOwner(memberId,storeId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/store/{storeId}/order/{orderId}")
    public ResponseEntity<OrderResponse> findOrderDetailForOwner(@RequestAttribute("id")Long memberId,
                                                                     @PathVariable(name ="storeId")Long storeId,
                                                                     @PathVariable(name ="orderId")Long orderId
    ){
        OrderResponse response = orderService.findOrderDetailForOwner(memberId, storeId, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
