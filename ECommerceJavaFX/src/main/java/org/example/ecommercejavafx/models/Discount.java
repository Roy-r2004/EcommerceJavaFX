package org.example.ecommercejavafx.models;

import java.time.LocalDateTime;

public class Discount {
    // Fields for Discount
    private int id;
    private String code;
    private String discountType; // "percentage" or "fixed"
    private double discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer usageLimit; // Optional field for usage limit

    // Constructors
    public Discount() {
    }

    public Discount(int id, String code, String discountType, double discountValue, LocalDateTime startDate, LocalDateTime endDate, Integer usageLimit) {
        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
    }

    public Discount(String code, String discountType, double discountValue, LocalDateTime startDate, LocalDateTime endDate, Integer usageLimit) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }



    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    // Utility Method to Check if Discount is Active
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return (now.isAfter(startDate) || now.isEqual(startDate)) && (now.isBefore(endDate) || now.isEqual(endDate));
    }

    @Override
    public String toString() {
        return "Discount{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", usageLimit=" + usageLimit +
                '}';
    }
}
