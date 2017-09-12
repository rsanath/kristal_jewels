package com.talenttakeaways.kristaljewels.product;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sanath on 10/09/17.
 */

public class ProductModel {

    DatabaseReference db;
    ProductPresenterInterface presenter;

    public ProductModel(ProductPresenterInterface presenter) {
        this.presenter = presenter;
        db = FirebaseDatabase.getInstance().getReference();
    }


}
