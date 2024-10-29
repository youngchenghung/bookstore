package com.example.bookstore.service;

import com.example.bookstore.dto.order.CreateOrderRequest;
import com.example.bookstore.dto.order.OrderQueryParams;
import com.example.bookstore.model.Order;


import java.util.List;

public interface OrderService {

    // 建立購物訂單
    Integer createOrder(Integer memberId, CreateOrderRequest createOrderRequest);

    // 透過訂單編號查詢訂單
    Order getOrderById(Integer orderId);

    // 計算訂單購物商品數量
    Integer countOrder(OrderQueryParams orderQueryParams);

    // 查詢購物訂單
    List<Order> getOrders(OrderQueryParams orderQueryParams);
}
