package com.example.btl_cnjava.entity;

public enum OrderStatus {
    PENDING("Chờ xử lý"),
    PAID("Đã thanh toán"),
    CANCELLED("Đã hủy");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
