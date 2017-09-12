package com.talenttakeaways.kristaljewels.cart;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.talenttakeaways.kristaljewels.models.CartItem;
import com.talenttakeaways.kristaljewels.others.Constants;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sanath on 10/09/17.
 */

public class CartModel implements ICartModel{

    SharedPreferences sp;
    ArrayList<CartItem> cartItems;

    public CartModel(Context context) {
        sp = context.getSharedPreferences(Constants.cartItems, MODE_PRIVATE);
        cartItems = getCartItems();
    }

    @Override
    public ArrayList<CartItem> getCartItems() {
        String itemsStringList = sp.getString(Constants.cartItems, null);
        if (itemsStringList == null) {
            return new ArrayList<>();
        }
        ArrayList<CartItem> cartItems = new Gson().fromJson(itemsStringList, Constants.cartItemsType);
        return cartItems;
    }

    @Override
    public int getItemsCount() {
        return cartItems.size();
    }

    @Override
    public void replaceItem(int position, CartItem item) {
        cartItems.set(position, item);
        writeChanges();
    }

    @Override
    public CartItem getItemAtPosition(int position) {
        return cartItems.get(position);
    }

    @Override
    public void removeItem(int index) {
        cartItems.remove(index);
        writeChanges();
    }

    public void writeChanges() {
        String itemsAsString = new Gson().toJson(cartItems);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.cartItems, itemsAsString).apply();
    }
}