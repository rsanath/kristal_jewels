package com.talenttakeaways.kristaljewels.cart;

import com.talenttakeaways.kristaljewels.models.CartItem;

import java.util.ArrayList;

/**
 * Created by sanath on 11/09/17.
 */

public interface ICartModel {

    ArrayList<CartItem> getCartItems();

    int getItemsCount();

    void replaceItem(int position, CartItem item);

    CartItem getItemAtPosition(int position);

    void removeItem(int index);
}
