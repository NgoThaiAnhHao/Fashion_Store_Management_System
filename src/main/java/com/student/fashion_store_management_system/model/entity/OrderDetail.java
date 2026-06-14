package com.student.fashion_store_management_system.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Order_Details")
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

    public OrderDetail() {
    }

    public OrderDetail(String maleSize, String femaleSize, String logoText, int pairQuantity, BigDecimal subTotal, Order order, Product product) {
        this.maleSize = maleSize;
        this.femaleSize = femaleSize;
        this.logoText = logoText;
        this.pairQuantity = pairQuantity;
        this.subTotal = subTotal;
        this.order = order;
        this.product = product;
    }

    public long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getMaleSize() {
        return maleSize;
    }

    public void setMaleSize(String maleSize) {
        this.maleSize = maleSize;
    }

    public String getFemaleSize() {
        return femaleSize;
    }

    public void setFemaleSize(String femaleSize) {
        this.femaleSize = femaleSize;
    }

    public String getLogoText() {
        return logoText;
    }

    public void setLogoText(String logoText) {
        this.logoText = logoText;
    }

    public int getPairQuantity() {
        return pairQuantity;
    }

    public void setPairQuantity(int pairQuantity) {
        this.pairQuantity = pairQuantity;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
