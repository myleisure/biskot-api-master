package com.biskot.domain.spi;

import com.biskot.domain.model.Product;

public interface ProductPort {

    Product getProduct(long productId);

}
