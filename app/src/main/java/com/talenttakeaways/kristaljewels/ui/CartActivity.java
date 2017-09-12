package com.talenttakeaways.kristaljewels.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.talenttakeaways.kristaljewels.R;
import com.talenttakeaways.kristaljewels.cart.CartItemAdapter;
import com.talenttakeaways.kristaljewels.cart.CartPresenter;
import com.talenttakeaways.kristaljewels.cart.ICartPresenter;
import com.talenttakeaways.kristaljewels.cart.ICartView;
import com.talenttakeaways.kristaljewels.models.Product;
import com.talenttakeaways.kristaljewels.others.CommonFunctions;
import com.talenttakeaways.kristaljewels.others.Constants;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanath on 23/06/17.
 */

public class CartActivity extends AppCompatActivity implements ICartView{
    ICartPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.cart_buynow_button) Button buyNowButton;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.info_card) CardView infoCard;
    @BindView(R.id.info_text) TextView infoText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        CommonFunctions.initNavigationDrawer(this, toolbar, true);
        infoCard.setVisibility(View.VISIBLE);
        buyNowButton.setVisibility(View.VISIBLE);
        presenter = new CartPresenter(this);
        CartItemAdapter adapter = new CartItemAdapter(presenter, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void proceedToPayment() {

    }

    @Override
    public void goToProductPage(Product product) {
        Intent intent = new Intent(this, ProductActivity.class);
        Parcelable productParcel = Parcels.wrap(product);
        intent.putExtra(Constants.product, productParcel);
        startActivity(intent);
    }

    @Override
    public void displayInfo(String info) {
        infoText.setText(info);
    }

    @Override
    public void setBuyNowButtonState(boolean state) {
        buyNowButton.setEnabled(state);
        if (state) {
            buyNowButton.setBackgroundColor(Color.parseColor("#ff7e14"));
        } else {
            buyNowButton.setBackgroundColor(Color.GRAY);
        }
    }

}
