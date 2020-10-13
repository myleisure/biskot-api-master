package com.biskot.infra.repository.entity;

import java.math.BigDecimal;

import com.biskot.domain.model.Product;

public class ProductEntity {
    
	private long id;
	
	private BigDecimal unitPrice;
	
	private long quantity;
	
	private String label;
	
	private int persitedEntity;
	
	public ProductEntity () {
		
	}
	
	public ProductEntity(Product product) {
		id = product.getId();
		unitPrice = product.getUnitPrice();
		quantity = product.getQuantity();
		label = product.getLabel();
		persitedEntity = product.getPersitedEntity();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getPersitedEntity() {
		return persitedEntity;
	}

	public void setPersitedEntity(int persitedEntity) {
		this.persitedEntity = persitedEntity;
	}
	
	
	
	
}
