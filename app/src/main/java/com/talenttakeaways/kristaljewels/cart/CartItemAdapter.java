package com.talenttakeaways.kristaljewels.cart;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.talenttakeaways.kristaljewels.models.CartItem;
import com.talenttakeaways.kristaljewels.models.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanath on 10/08/17.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder> {

    ICartPresenter presenter;
    Context context;

    public CartItemAdapter(ICartPresenter presenter, Context context) {
        this.presenter = presenter;
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

        final CartItem cartItem = presenter.getCartItemAtPosition(position);
        final Product product = cartItem.getProduct();

        holder.itemName.setText(product.getProductName());
        holder.itemPrice.setText(product.getProductPrice() + " $");
        holder.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.itemColor.setText(cartItem.getColor());
        holder.itemSize.setText(cartItem.getSize());
        Glide.with(context)
                .load(product.getProductImages().get(1))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.default_placeholder)
                .into(holder.itemImage);

        holder.itemDecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(holder.itemQuantity.getText().toString());
                if (currentQuantity <= 1) {
                    return;
                }
                holder.itemQuantity.setText(String.valueOf(--currentQuantity));
                presenter.handleDecButtonClick(position);
            }
        });

        holder.itemIncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(holder.itemQuantity.getText().toString());
                holder.itemQuantity.setText(String.valueOf(++currentQuantity));
                presenter.handleIncButtonClick(position);
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .title("Confirm ?")
                        .content("Remove " + cartItem.getProduct().getProductName())
                        .negativeText("Cancel")
                        .positiveText("Yes")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
                                presenter.handleRemoveItem(position);
                                notifyDataSetChanged();
                            }
                        }).show();
            }
        });

        holder.cartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.handleItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cart_name) TextView itemName;
        @BindView(R.id.cart_price) TextView itemPrice;
        @BindView(R.id.cart_item_color) TextView itemColor;
        @BindView(R.id.cart_item_size) TextView itemSize;
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
