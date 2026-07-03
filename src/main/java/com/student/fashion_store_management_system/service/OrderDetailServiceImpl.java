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
            // Calculate total quantity for this cart item
            int totalQuantity = item.getMember1Quantity() + item.getMember2Quantity();

            // Decrease stock quantity for the product
            productService.degreeStockQuantity(
                    item.getProduct().getProductId(),
                    totalQuantity
            );

            // Save new OrderDetail
            orderDetailRepository.save(
                    new OrderDetail(
                            item.getMember1Size(),
                            item.getMember2Size(),
                            item.getMember1Gender(),
                            item.getMember2Gender(),
                            item.getCustomLogoText(),
                            item.getCustomLogoImageUrl(),
                            item.getLogoSize(), // Add logoSize
                            item.getLogoPosition(), // Add logoPosition
                            item.getMember1Quantity(),
                            item.getMember2Quantity(),
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