package com.talenttakeaways.kristaljewels;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.talenttakeaways.kristaljewels.adapters.ProductsAdapter;
import com.talenttakeaways.kristaljewels.beans.Product;
import com.talenttakeaways.kristaljewels.others.CommonFunctions;
import com.talenttakeaways.kristaljewels.others.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanath on 13/06/17.
 */

public class ProductListActivity extends AppCompatActivity {

    ProductsAdapter productsAdapter;
    ArrayList<Product> productsList = new ArrayList<Product>();

    //database objects
    FirebaseUser fbUser;
    DatabaseReference dbRef;
    Query query;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.info_card) CardView infoCard;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.info_text) TextView infoText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Activity context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(context);
        setSupportActionBar(toolbar);
        if (!CommonFunctions.isNetworkAvailable(context)) {
            infoCard.setVisibility(View.VISIBLE);
            infoText.setText("No Internet\nPlease check your network and try again");
        }
        infoCard.setVisibility(View.VISIBLE);

        CommonFunctions.initNavigationDrawer(context, toolbar, true);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        productsAdapter = new ProductsAdapter(this, productsList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menuSearch));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra(Constants.from, Constants.search);
                intent.putExtra(Constants.search, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem cartButton = menu.findItem(R.id.open_cart);
        cartButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void loadData() {
        Intent intent = getIntent();
        String from, categoryName, searchQueryName;

        from = intent.getExtras().getString("from");

        if (from.equals("button")) {
            categoryName = intent.getExtras().getString("category");
            toolbar.setTitle(categoryName);
            dbRef.child("products").orderByChild("productCategory").equalTo(categoryName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot products : dataSnapshot.getChildren()) {
                                Product product = products.getValue(Product.class);
                                Log.d("Category Results ", product.getProductName() + product.getProductColors());
                                productsList.add(product);
                            }
                            infoCard.setVisibility(View.GONE);
                            recyclerView.setAdapter(productsAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (from.equals("search")) {
            searchQueryName = intent.getExtras().getString("search");
            toolbar.setTitle("Results: " + searchQueryName);
            dbRef.child("products").orderByChild("productTag").startAt(searchQueryName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot products : dataSnapshot.getChildren()) {
                                Product product = products.getValue(Product.class);
                                Log.d("Search Results ", product.getProductName() + product.getProductColors());
                                productsList.add(product);
                            }
                            infoCard.setVisibility(View.GONE);
                            recyclerView.setAdapter(productsAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
