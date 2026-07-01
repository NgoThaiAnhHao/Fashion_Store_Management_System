package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.entity.CartItem;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.OrderDetail;
import com.student.fashion_store_management_system.repository.OrderDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public void addNew(List<CartItem> cartItems, Order order) {
        cartItems.forEach(item -> {
            productService.degreeStockQuantity(
                    item.getProduct().getProductId(),
                    item.getPairQuantity() * 2
            );

            orderDetailRepository.save(
                    new OrderDetail(
                            item.getMaleSize(),
                            item.getFemaleSize(),
                            item.getCustomLogoText(),
                            item.getCustomLogoImageUrl(),
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