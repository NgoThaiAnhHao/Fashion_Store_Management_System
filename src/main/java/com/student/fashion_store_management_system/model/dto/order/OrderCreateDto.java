package com.student.fashion_store_management_system.model.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {
    @NotBlank(message = "* Receiver name is mandatory!")
    private String receiverName;

    @NotBlank(message = "* Phone is mandatory!")
    private String receiverPhone;

    @NotBlank(message = "* Address is mandatory!")
    private String shippingAddress;

    @NotBlank(message = "* City is mandatory!")
    private String city;

    @NotBlank(message = "* Zipcode is mandatory!")
    private String zipcode;
}
