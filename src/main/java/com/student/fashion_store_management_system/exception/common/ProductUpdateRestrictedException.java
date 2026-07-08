package com.student.fashion_store_management_system.exception.common;

public class ProductUpdateRestrictedException extends RuntimeException {
    public ProductUpdateRestrictedException(String message) {
        super(message);
    }
}