package com.student.fashion_store_management_system.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Order_Details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private long orderDetailId;

    @Column(name = "male_size", nullable = false)
    private String maleSize;

    @Column(name = "female_size", nullable = false)
    private String femaleSize;

    private String logoText;

    @Column(name = "pair_quantity", nullable = false)
    private int pairQuantity;

    @Column(name = "sub_total", nullable = false)
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderDetail(String maleSize, String femaleSize, String logoText, int pairQuantity, BigDecimal subTotal, Order order, Product product) {
        this.maleSize = maleSize;
        this.femaleSize = femaleSize;
        this.logoText = logoText;
        this.pairQuantity = pairQuantity;
        this.subTotal = subTotal;
        this.order = order;
        this.product = product;
    }
}
