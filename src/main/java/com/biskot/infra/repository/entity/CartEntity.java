package com.biskot.infra.repository.entity;

import java.util.List;

public class CartEntity {
    
	private long id;
	
	private List<ProductEntity> productEntities;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<ProductEntity> getProductEntities() {
		return productEntities;
	}

	public void setProductEntities(List<ProductEntity> productEntities) {
		this.productEntities = productEntities;
	}
	
	
}
