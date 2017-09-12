package com.talenttakeaways.kristaljewels.cart;

import android.content.Context;

import com.talenttakeaways.kristaljewels.models.Product;

/**
 * Created by sanath on 10/09/17.
 */

public interface ICartView {

    Context getContext();

    void proceedToPayment();

    void displayInfo(String info);

    void setBuyNowButtonState(boolean state);

    void goToProductPage(Product product);

}
