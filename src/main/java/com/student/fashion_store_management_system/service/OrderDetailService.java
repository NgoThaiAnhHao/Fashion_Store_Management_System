package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.OrderStatusEnum;
import com.student.fashion_store_management_system.model.entity.CartItem;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.OrderDetail;
import com.student.fashion_store_management_system.model.entity.User;

import java.util.List;

public interface OrderDetailService {
    void addNew(List<CartItem> cartItems, Order order);

    List<OrderDetail> findAllByOrder(Order order);
}
