package com.talenttakeaways.kristaljewels.cart;

import com.talenttakeaways.kristaljewels.models.CartItem;
import com.talenttakeaways.kristaljewels.models.Product;

/**
 * Created by sanath on 11/09/17.
 */

public interface ICartPresenter {

    CartItem getCartItemAtPosition(int position);

    int getItemCount();

    void handleIncButtonClick(int index);

    void handleDecButtonClick(int index);

    void handleRemoveItem(int index);

    void handleItemClick(Product product);
}
