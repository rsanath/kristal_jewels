package com.talenttakeaways.kristaljewels.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.talenttakeaways.kristaljewels.ProductListActivity;
import com.talenttakeaways.kristaljewels.R;
import com.talenttakeaways.kristaljewels.adapters.CommentsAdapter;
import com.talenttakeaways.kristaljewels.models.CartItem;
import com.talenttakeaways.kristaljewels.models.Product;
import com.talenttakeaways.kristaljewels.models.Review;
import com.talenttakeaways.kristaljewels.models.User;
import com.talenttakeaways.kristaljewels.others.CommonFunctions;
import com.talenttakeaways.kristaljewels.others.Constants;
import com.talenttakeaways.kristaljewels.product.ProductViewInterface;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductActivity extends AppCompatActivity implements ProductViewInterface{
    Activity context = this;

    FirebaseUser fbUser;
    DatabaseReference db;

    TextView productName, productPrice, productDescription;
    ImageView productImage;
    Spinner productColor, productSize;
    ExpandableHeightListView productComments;
    Product product;
    Button commentButton, buyNow;
    CommentsAdapter myCommentsAdapter;

    @BindView(R.id.add_to_cart) Button addToCartButton;
    @BindView(R.id.review_section) LinearLayout reviewSection;
    @BindView(R.id.review_section_switch) TextView reviewSectionSwitch;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(context);
        setSupportActionBar(toolbar);

        reviewSection.setVisibility(View.GONE);

        Intent intent = getIntent();
        product = Parcels.unwrap(intent.getParcelableExtra("product"));
        db = FirebaseDatabase.getInstance().getReference();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(product.getProductName());
        setSupportActionBar(toolbar);
        setView();
        CommonFunctions.initNavigationDrawer(context, toolbar, true);
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

    public void setView() {
        loadComments();
        productImage = (ImageView) findViewById(R.id.product_cover);
        productName = (TextView) findViewById(R.id.product_name);
        productPrice = (TextView) findViewById(R.id.product_price);
        productDescription = (TextView) findViewById(R.id.product_description);
        commentButton = (Button) findViewById(R.id.comment_button);
        productComments = (ExpandableHeightListView) findViewById(R.id.product_comments);
        productSize = (Spinner) findViewById(R.id.product_size);
        productColor = (Spinner) findViewById(R.id.product_color);
        addToCartButton = (Button) findViewById(R.id.add_to_cart);

        // Set all values
        if (product.getProductImages() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Glide.with(this).load(product.getProductImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(width, width).into(productImage);
        }
        productName.setText(product.getProductName());
        productPrice.setText("Rs. " + product.getProductPrice());
        productDescription.setText(product.getProductDescription());

        ArrayAdapter<String> colorAdapter;
        colorAdapter = new ArrayAdapter<>(ProductActivity.this, R.layout.support_simple_spinner_dropdown_item,
                product.getProductColors().toArray(new String[0]));
        productColor.setAdapter(colorAdapter);

        ArrayAdapter<String> sizeAdapter;
        sizeAdapter = new ArrayAdapter<>(ProductActivity.this,
                R.layout.support_simple_spinner_dropdown_item, product.getProductSizes().toArray(new String[1]));
        productSize.setAdapter(sizeAdapter);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CartItem> cartItems = CommonFunctions.getCartItems(context);
                for (CartItem item : cartItems) {
                    if (item.getProduct().getProductId().equals(product.getProductId())) {
                        CommonFunctions.showToast(context, "Already in Cart");
                        return;
                    }
                }
                cartItems.add(new CartItem(product, 1, productColor.getSelectedItem().toString(),
                        productSize.getSelectedItem().toString()));
                CommonFunctions.setCartItems(context, cartItems);
                CommonFunctions.showToast(context, product.getProductName() + " added to cart");
            }
        });

        reviewSectionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewSection.getVisibility() == View.GONE) {
                    reviewSection.setVisibility(View.VISIBLE);
                    reviewSection.setAlpha(0.0f);
                    reviewSection.animate().alpha(1.0f).setDuration(500).start();
                } else {
                    reviewSection.animate().alpha(0.0f).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            reviewSection.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    public void addComment() {

        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences(Constants.currentUser, MODE_PRIVATE);
        String userString = sp.getString(Constants.currentUser, null);
        final User user = gson.fromJson(userString, User.class);

        final MaterialDialog commentDialog = new MaterialDialog.Builder(context)
                .title(R.string.review_title)
                .customView(R.layout.dialog_add_review, false)
                .positiveText(R.string.add)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(final MaterialDialog dialog, DialogAction which) {
                        View v = dialog.getCustomView();
                        EditText userReviewText = (EditText) v.findViewById(R.id.user_review);
                        RatingBar userRatingBar = (RatingBar) v.findViewById(R.id.user_rating);
                        String userReview = userReviewText.getText().toString();
                        float userRating = userRatingBar.getRating();

                        Review review = new Review();
                        review.setReviewAuthorId(user.getUserId());
                        review.setReviewAuthorName(user.getName());
                        review.setReviewMessage(userReview);
                        review.setReviewRating(String.valueOf(userRating));
                        String reviewId = db.push().getKey();
                        review.setReviewId(reviewId);

                        db.child(Constants.comments)
                                .child(product.getProductId())
                                .child(reviewId).setValue(review)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isComplete()){
                                            loadComments();
                                        }
                                    }
                                });
                    }
                })
                .negativeText(R.string.cancel)
                .show();

        commentDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

        EditText userReview = (EditText) commentDialog.getView().findViewById(R.id.user_review);

                userReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 20) {
                    commentDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                } else {
                    commentDialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                }
            }
        });
    }

    public void loadComments(){
        final ArrayList<Review> comments = new ArrayList<Review>();
        db.child(Constants.comments).child(product.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> commentTexts = new ArrayList<String>();
                for(DataSnapshot commentsResult : dataSnapshot.getChildren()){
                    Review comment = commentsResult.getValue(Review.class);
                    comments.add(comment);
                }
                myCommentsAdapter = new CommentsAdapter(getApplicationContext(), comments);
                productComments.setAdapter(myCommentsAdapter);
                productComments.setExpanded(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.add_to_cart)
    @Override
    public void addToCartButtonHandler() {

    }

    @Override
    public void buyNowButtonHandler() {

    }

    @Override
    public void showCommentDialog() {

    }
}
