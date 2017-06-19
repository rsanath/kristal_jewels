package com.talenttakeaways.kristaljewels.beans;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by sanath on 13/06/17.
 */

@Parcel
public class Product {
    public String productId, productName, productPrice, productDescription, productCategory, productRating, productStockLeft;
    public ArrayList<String> productImages, productSizes, productColors, productTags;

    public Product() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public ArrayList<String> getProductTags() {
        return productTags;
    }

    public void setProductTags(ArrayList<String> productTags) {
        this.productTags = productTags;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductRating() {
        return productRating;
    }

    public void setProductRating(String productRating) {
        this.productRating = productRating;
    }

    public String getProductStockLeft() {
        return productStockLeft;
    }

    public void setProductStockLeft(String productStockLeft) {
        this.productStockLeft = productStockLeft;
    }

    public ArrayList<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(ArrayList<String> productImages) {
        this.productImages = productImages;
    }

    public ArrayList<String> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(ArrayList<String> productSizes) {
        this.productSizes = productSizes;
    }

    public ArrayList<String> getProductColors() {
        return productColors;
    }

    public void setProductColors(ArrayList<String> productColors) {
        this.productColors = productColors;
    }

}
