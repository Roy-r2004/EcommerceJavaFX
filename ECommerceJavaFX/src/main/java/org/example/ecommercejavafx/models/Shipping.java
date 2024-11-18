package org.example.ecommercejavafx.models;

import java.time.LocalDateTime;

public class Shipping {
    private int shippingId;
    private int orderId;
    private String shippingProvider;
    private String trackingNumber;
    private LocalDateTime estimatedDelivery;
    private String shippingStatus;

    public Shipping(int shippingId, int orderId, String shippingProvider, String trackingNumber, LocalDateTime estimatedDelivery, String shippingStatus) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.shippingProvider = shippingProvider;
        this.trackingNumber = trackingNumber;
        this.estimatedDelivery = estimatedDelivery;
        this.shippingStatus = shippingStatus;
    }

    // Getters and Setters
    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getShippingProvider() {
        return shippingProvider;
    }

    public void setShippingProvider(String shippingProvider) {
        this.shippingProvider = shippingProvider;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDateTime getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(LocalDateTime estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }
}
