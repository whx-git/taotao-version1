package com.whx.taotao.order;

import java.io.Serializable;
import java.util.List;

public class OrderBean implements Serializable {
    private List<OrderItem> orderItems;
    private OrderShipping orderShipping;
    private Integer payment;

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(OrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }

}
