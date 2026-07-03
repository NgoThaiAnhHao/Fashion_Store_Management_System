package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.OrderStatusEnum;
import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.mapper.OrderMapper;
import com.student.fashion_store_management_system.model.dto.order.OrderCreateDto;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findMyOrders() {
        // Get current user
        User currentUser = authenticationService.getCurrentUser();
        return orderRepository.findAllByOrderedBy(currentUser);
    }

    @Override
    public Order findById(long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("ORDER NOT FOUND")
                );
    }

    @Override
    @Transactional
    public Order addNew(OrderCreateDto orderCreateDto, BigDecimal totalAmount) {
        // Get current user
        User currentUser = authenticationService.getCurrentUser();

        return orderRepository.save(
                OrderMapper.toEntity(orderCreateDto, currentUser, totalAmount)
        );
    }

    @Override
    public void updateStatus(long id, OrderStatusEnum status, String rejectReason) {
        Order order = findById(id);
        order.setStatus(status);
        if (status == OrderStatusEnum.LOGO_REJECTED) {
            order.setRejectReason(rejectReason);
        } else {
            order.setRejectReason(null);
        }
        orderRepository.save(order);
    }

    @Override
    public List<Order> findByUserFullName(String fullName) {
        return orderRepository.findByOrderedBy_FullNameContainingIgnoreCase(fullName);
    }

}