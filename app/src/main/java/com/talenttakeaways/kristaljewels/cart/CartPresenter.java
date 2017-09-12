package com.talenttakeaways.kristaljewels.cart;

import com.talenttakeaways.kristaljewels.models.CartItem;
import com.talenttakeaways.kristaljewels.models.Product;

import java.util.ArrayList;

/**
 * Created by sanath on 10/09/17.
 */

public class CartPresenter implements ICartPresenter{

    ICartView view;
    ICartModel model;

    public CartPresenter(ICartView view) {
        this.view = view;
        model = new CartModel(view.getContext());
        updateView();
    }

    @Override
    public CartItem getCartItemAtPosition(int position) {
        return model.getItemAtPosition(position);
    }

    @Override
    public int getItemCount() {
        return model.getItemsCount();
    }

    void updateView() {
        ArrayList<CartItem> items = model.getCartItems();
        boolean itemsPresent = items.size() >= 1;

        view.setBuyNowButtonState(itemsPresent);
        if (itemsPresent) {
            int totalPrice = 0;
            for (CartItem i : items) {
                int price = Integer.parseInt(i.getProduct().getProductPrice());
                int quantity = i.getQuantity();
                totalPrice += price * quantity;
            }
            String message = "Total Price - " + totalPrice + " $";
            view.displayInfo(String.valueOf(message));
        } else {
            view.displayInfo("Nothing in Cart");
        }
    }

    @Override
    public void handleIncButtonClick(int index) {
        CartItem item = model.getItemAtPosition(index);
        item.quantity = ++item.quantity;
        model.replaceItem(index, item);
        updateView();
    }

    @Override
    public void handleDecButtonClick(int index) {
        CartItem item = model.getItemAtPosition(index);
        item.quantity = --item.quantity;
        model.replaceItem(index, item);
        updateView();
    }

    @Override
    public void handleRemoveItem(int index) {
        model.removeItem(index);
        updateView();
    }

    @Override
    public void handleItemClick(Product product) {
        view.goToProductPage(product);
    }
}
