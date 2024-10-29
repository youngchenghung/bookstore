package com.example.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.dto.order.CreateOrderRequest;
import com.example.bookstore.dto.order.OrderQueryParams;
import com.example.bookstore.service.OrderService;
import com.example.bookstore.util.Page;
import com.example.bookstore.model.Order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

@RestController
@Validated
public class OrderController {

    @Autowired 
    private OrderService orderService;

    // 查詢購物訂單
    @GetMapping("member/{memberId}/order-list")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer memberId,
            @RequestParam(defaultValue = "dsc") String sort,
            @RequestParam(defaultValue = "1") @Max(100) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) 
            {
                OrderQueryParams orderQueryParams = new OrderQueryParams();
                orderQueryParams.setMemberId(memberId);
                orderQueryParams.setLimit(limit);
                orderQueryParams.setOffset(offset);

                List<Order> orderList = orderService.getOrders(orderQueryParams);

                Integer count = orderService.countOrder(orderQueryParams);

                Page<Order> page = new Page<>();
                page.setLimit(limit);
                page.setOffset(offset);
                page.setTotal(offset);
                page.setResult(orderList);

                return ResponseEntity.status(HttpStatus.OK).body(page);
            }
    
    // 建立購物訂單
    @PostMapping("member/{memberId}/create-order")
    public ResponseEntity<?> createOrder(
            @PathVariable Integer memberId, 
            @RequestBody @Valid CreateOrderRequest createOrderRequest) 
            {
                Integer orderId = orderService.createOrder(memberId, createOrderRequest);

                Order order = orderService.getOrderById(orderId);

                return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
