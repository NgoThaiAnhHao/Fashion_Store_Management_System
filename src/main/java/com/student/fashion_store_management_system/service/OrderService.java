package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.OrderStatusEnum;
import com.student.fashion_store_management_system.model.dto.order.OrderCreateDto;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<Order> findAll();

    List<Order> findMyOrders();

    Order findById(long orderId);

    Order addNew(OrderCreateDto orderCreateDto, BigDecimal totalAmount);

    void updateStatus(long id, OrderStatusEnum status);

    List<Order> findByUserFullName(String fullName);
}
