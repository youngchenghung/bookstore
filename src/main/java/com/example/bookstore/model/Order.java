package com.example.bookstore.model;

import java.sql.Timestamp;

import java.util.List;

public class Order {

    private Integer orderId;
    private Integer memberId;
    private Integer totalAmount;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;

    private List<OrderItem> orderItemList;


    public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    public Integer getMemberId() {
        return memberId;
    }
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
    public Integer getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }
    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }    
    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }
    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
