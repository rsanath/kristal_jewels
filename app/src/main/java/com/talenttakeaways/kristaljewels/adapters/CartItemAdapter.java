package com.talenttakeaways.kristaljewels.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.talenttakeaways.kristaljewels.R;
import com.talenttakeaways.kristaljewels.beans.CartItem;
import com.talenttakeaways.kristaljewels.beans.Product;
import com.talenttakeaways.kristaljewels.others.CommonFunctions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanath on 10/08/17.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder> {

    ArrayList<CartItem> cartItems;
    Context context;

    public CartItemAdapter(ArrayList<CartItem> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cart_item, parent, false);
        return new CartItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartItemHolder holder, final int position) {
        final CartItem cartItem = cartItems.get(position);
        final Product product = cartItem.getProduct();

        holder.itemName.setText(product.getProductName());
        holder.itemPrice.setText(product.getProductPrice());
        holder.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(context)
                .load(product.getProductImages().get(1))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.default_placeholder)
                .into(holder.itemImage);

        holder.itemDecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(holder.itemQuantity.getText().toString());
                if (q <= 1) {return;}
                q -= 1;
                cartItem.setQuantity(q);
                cartItems.set(position, cartItem);
                CommonFunctions.setCartItems(context, cartItems);
                holder.itemQuantity.setText(String.valueOf(q));
            }
        });

        holder.itemIncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(holder.itemQuantity.getText().toString());
                q += 1;
                cartItem.setQuantity(q);
                cartItems.set(position, cartItem);
                CommonFunctions.setCartItems(context, cartItems);
                holder.itemQuantity.setText(String.valueOf(q));
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog.Builder dialog = CommonFunctions.getDismissDialog(context, "Confirm",
                                "Remove "+product.getProductName()+ " ?");
                dialog.negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        cartItems.remove(position);
                        CommonFunctions.setCartItems(context, cartItems);
                        notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.cart_name) TextView itemName;
        @BindView(R.id.cart_price) TextView itemPrice;
        @BindView(R.id.cart_item_quantity) TextView itemQuantity;
        @BindView(R.id.cart_inc_quantity) Button itemIncButton;
        @BindView(R.id.cart_dec_quantity) Button itemDecButton;
        @BindView(R.id.cart_image) ImageView itemImage;
        @BindView(R.id.cart_remove_item) ImageView removeButton;
        @BindView(R.id.cart_item) LinearLayout cartItem;

        public CartItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
