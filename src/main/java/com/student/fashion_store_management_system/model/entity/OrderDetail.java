package com.student.fashion_store_management_system.model.entity;

import com.student.fashion_store_management_system.enums.Gender; // Import new Gender enum
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

    @Column(name = "member1_size", nullable = false) // Renamed
    private String member1Size;

    @Column(name = "member2_size", nullable = false) // Renamed
    private String member2Size;

    @Enumerated(EnumType.STRING) // New field
    @Column(name = "member1_gender", nullable = false)
    private Gender member1Gender;

    @Enumerated(EnumType.STRING) // New field
    @Column(name = "member2_gender", nullable = false)
    private Gender member2Gender;

    @Column(name = "logo_text")
    private String logoText;

    @Column(name = "logo_image_url")
    private String logoImageUrl;

    @Column(name = "logo_size") // New field for logo size
    private String logoSize;

    @Column(name = "logo_position") // New field for logo position
    private String logoPosition;

    @Column(name = "member1_quantity", nullable = false) // Renamed
    private int member1Quantity;

    @Column(name = "member2_quantity", nullable = false) // New field
    private int member2Quantity;

    @Column(name = "sub_total", nullable = false)
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Updated constructor
    public OrderDetail(String member1Size,
                       String member2Size,
                       Gender member1Gender,
                       Gender member2Gender,
                       String logoText,
                       String logoImageUrl,
                       String logoSize, // Add logoSize to constructor
                       String logoPosition, // Add logoPosition to constructor
                       int member1Quantity,
                       int member2Quantity,
                       BigDecimal subTotal,
                       Order order,
                       Product product) {
        this.member1Size = member1Size;
        this.member2Size = member2Size;
        this.member1Gender = member1Gender;
        this.member2Gender = member2Gender;
        this.logoText = logoText;
        this.logoImageUrl = logoImageUrl;
        this.logoSize = logoSize; // Initialize logoSize
        this.logoPosition = logoPosition; // Initialize logoPosition
        this.member1Quantity = member1Quantity;
        this.member2Quantity = member2Quantity;
        this.subTotal = subTotal;
        this.order = order;
        this.product = product;
    }
}