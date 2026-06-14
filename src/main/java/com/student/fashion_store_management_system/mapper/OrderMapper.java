package com.student.fashion_store_management_system.mapper;

import com.student.fashion_store_management_system.model.dto.order.OrderCreateDto;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.User;

import java.math.BigDecimal;

public class OrderMapper {
    public static Order toEntity(OrderCreateDto orderCreateDto, User currentUser, BigDecimal totalAmount) {
        return new Order(
                totalAmount,
                orderCreateDto.getShippingAddress(),
                orderCreateDto.getReceiverName(),
                orderCreateDto.getReceiverPhone(),
                orderCreateDto.getCity(),
                orderCreateDto.getZipcode(),
                currentUser
        );
    }
}
