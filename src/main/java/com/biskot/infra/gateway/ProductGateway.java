package com.biskot.infra.gateway;

import org.springframework.stereotype.Component;

import com.biskot.domain.model.Product;
import com.biskot.domain.spi.ProductPort;
import org.springframework.stereotype.Repository;

@Repository
public class ProductGateway implements ProductPort {

    public Product getProduct(long productId) {
        // TODO: to be implemented
        return null;
    }

}
