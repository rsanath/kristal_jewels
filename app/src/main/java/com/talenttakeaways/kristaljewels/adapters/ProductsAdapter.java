package com.talenttakeaways.kristaljewels.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.talenttakeaways.kristaljewels.ProductDetailActivity;
import com.talenttakeaways.kristaljewels.R;
import com.talenttakeaways.kristaljewels.beans.Product;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanath on 13/06/17.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder> {

    private Context mContext;
    private List<Product> productsList;

    public ProductsAdapter(Context mContext, ArrayList<Product> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;
    }

    public class ProductsHolder extends RecyclerView.ViewHolder {

        public TextView title, price, rating;
        public ImageView cover;
        public CardView productCard;

        public ProductsHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.product_title);
            price = (TextView) view.findViewById(R.id.product_price);
            rating = (TextView) view.findViewById(R.id.product_rating);
            cover = (ImageView) view.findViewById(R.id.product_cover);
            productCard = (CardView) view.findViewById(R.id.product_card);
        }

    }

        @Override
        public ProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_card_product, parent, false);
            return new ProductsHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProductsHolder holder, int position) {
            final Product product = productsList.get(position);
            holder.title.setText(product.getProductName());
            holder.price.setText(product.getProductPrice());
            holder.rating.setText(product.getProductRating());
            if(product.getProductImages() != null){
                Glide.with(mContext).load(product.getProductImages().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.def_img).into(holder.cover);
            }

            //Adds the clicked product info and starts the productdeails activity
            holder.productCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("product", Parcels.wrap(product));
                    mContext.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return productsList.size();
        }
    }
