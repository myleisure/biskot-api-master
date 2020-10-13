package com.biskot.domain.model;

import java.math.BigDecimal;

import com.biskot.infra.repository.entity.ProductEntity;

public class Product {
	private long id;
	
	private BigDecimal unitPrice;
	
	private long quantity;
	
	private String label;
	
	private int persitedEntity;
	
	public Product() {
		
	}
	
	public Product(ProductEntity productEntity) {
		id = productEntity.getId();
		unitPrice = productEntity.getUnitPrice();
		quantity = productEntity.getQuantity();
		label = productEntity.getLabel();
		persitedEntity = productEntity.getPersitedEntity();
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
