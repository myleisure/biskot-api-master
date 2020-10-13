package com.biskot.infra.gateway.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("label")
    private String label;

    @JsonProperty("unit_price")
    private double unitPrice;

    @JsonProperty("quantity_in_stock")
    private int quantityInStock;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
