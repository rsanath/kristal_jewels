package com.talenttakeaways.kristaljewels.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by sanath on 13/06/17.
 */

@Parcel
public class Product {
    public String productId, productName, productPrice, productDescription, productCategory,
            productTag, productStockLeft;
    public ArrayList<String> productImages, productSizes, productColors;

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

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
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

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productTag='" + productTag + '\'' +
                ", productStockLeft='" + productStockLeft + '\'' +
                ", productImages=" + productImages +
                ", productSizes=" + productSizes +
                ", productColors=" + productColors +
                '}';
    }
}
