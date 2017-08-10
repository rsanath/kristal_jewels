package com.talenttakeaways.kristaljewels;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.talenttakeaways.kristaljewels.adapters.CartItemAdapter;
import com.talenttakeaways.kristaljewels.beans.CartItem;
import com.talenttakeaways.kristaljewels.others.CommonFunctions;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanath on 23/06/17.
 */

public class CartActivity extends AppCompatActivity{
    Activity context = this;

    //database objects
    FirebaseUser fbUser;
    DatabaseReference dbRef;

    //cart set
    HashSet<String> cartItems;
    SharedPreferences sp;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        CommonFunctions.initNavigationDrawer(context, toolbar, true);

        ArrayList<CartItem> cartItems = CommonFunctions.getCartItems(context);

        CartItemAdapter cartAdapter = new CartItemAdapter(cartItems, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

    }
}
