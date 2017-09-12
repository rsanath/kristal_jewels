package com.talenttakeaways.kristaljewels.models;

/**
 * Created by sanath on 10/09/17.
 */

public class ProductUnit {
    public String productId, productName, productPrice, productColor, productSize;

    public ProductUnit(String productId, String productName, String productPrice,
                       String productColor, String productSize) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productColor = productColor;
        this.productSize = productSize;
    }

    public ProductUnit() {
    }
}
