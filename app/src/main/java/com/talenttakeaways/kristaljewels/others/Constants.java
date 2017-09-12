package com.talenttakeaways.kristaljewels.others;

import com.google.gson.reflect.TypeToken;
import com.talenttakeaways.kristaljewels.models.CartItem;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by sanath on 12/07/17.
 */

public class Constants {
    public static final String from = "from";
    public static final String button = "button";
    public static final String category = "category";
    public static final String necklace = "necklace";
    public static final String bangle = "bangle";
    public static final String ring = "ring";
    public static final String earring = "earring";
    public static final String search = "search";
    public static final String users = "users";
    public static final String currentUser = "CURRENT_USER";
    public static final String comments = "comments";
    public static final String cartItems = "CART";
    public static final String product = "product";
    public static final Type cartItemsType = new TypeToken<ArrayList<CartItem>>(){}.getType();

}
