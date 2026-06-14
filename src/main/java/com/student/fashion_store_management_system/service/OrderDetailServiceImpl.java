package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.entity.*;
import com.student.fashion_store_management_system.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, ProductService productService) {
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
    }

    @Override
    @Transactional
    public void addNew(List<CartItem> cartItems, Order order) {
        cartItems.forEach(item -> {
            // Decrease stock quantity of product in db
            productService.degreeStockQuantity(item.getProduct().getProductId(), item.getPairQuantity() * 2);

            // Saving to db
            orderDetailRepository.save(
                    new OrderDetail(
                            item.getMaleSize(),
                            item.getFemaleSize(),
                            item.getCustomLogoText(),
                            item.getPairQuantity(),
                            item.getTotalSalePriceByPairQuantity(),
                            order,
                            item.getProduct()
                    )
            );
        });
    }

    @Override
    public List<OrderDetail> findAllByOrder(Order order) {
        return orderDetailRepository.findAllByOrder(order);
    }


}
