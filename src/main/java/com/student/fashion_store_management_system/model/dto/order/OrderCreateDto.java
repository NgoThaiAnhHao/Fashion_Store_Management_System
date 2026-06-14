package com.student.fashion_store_management_system.model.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    public OrderCreateDto() {
    }

    public OrderCreateDto(String receiverName, String receiverPhone, String shippingAddress, String city, String zipcode) {
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.shippingAddress = shippingAddress;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
