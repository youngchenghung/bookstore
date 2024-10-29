package com.example.bookstore.dao;

import com.example.bookstore.dto.order.OrderQueryParams;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;

import java.util.List;

public interface OrderDao {

    // 建立購物訂單
    Integer createOrder(Integer memberId, Integer totalAmount);

    // 建立購物訂單商品
    void createOrderItem(Integer orderId, List<OrderItem> orderItemList);

    // 透過訂單編號查詢訂單
    Order getOrderById(Integer orderId);

    // 透過訂單編號查詢訂單商品
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    // 計算訂單購物商品數量
    Integer countOrder(OrderQueryParams orderQueryParams);

    // 查詢購物訂單
    List<Order> getOrders(OrderQueryParams orderQueryParams);
}
