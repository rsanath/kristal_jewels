package com.talenttakeaways.kristaljewels;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talenttakeaways.kristaljewels.adapters.CommentsAdapter;
import com.talenttakeaways.kristaljewels.beans.Product;
import com.talenttakeaways.kristaljewels.beans.Review;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;

public class ProductDetailActivity extends AppCompatActivity {

    FirebaseUser fbUser;
    DatabaseReference db;

    TextView productName, productPrice, productDescription;
    ImageView productImage;
    LinearLayout reviewSection;
    Spinner productColor, productSize;
    ExpandableHeightListView productComments;
    Product product;
    Button commentButton, addToCart, buyNow;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_detail);

        Intent intent = getIntent();
        product = Parcels.unwrap(intent.getParcelableExtra("product"));
        db = FirebaseDatabase.getInstance().getReference();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(product.getProductName());
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        setView();

    }

    public void setView() {
        loadComments();
        productImage = (ImageView) findViewById(R.id.product_cover);
        productName = (TextView) findViewById(R.id.product_name);
        productPrice = (TextView) findViewById(R.id.product_price);
        productDescription = (TextView) findViewById(R.id.product_description);
        commentButton = (Button) findViewById(R.id.comment_button);
        productComments = (ExpandableHeightListView) findViewById(R.id.product_comments);
        reviewSection = (LinearLayout) findViewById(R.id.review_section);
        productSize = (Spinner) findViewById(R.id.product_size);
        productColor = (Spinner) findViewById(R.id.product_color);
        addToCart = (Button) findViewById(R.id.add_to_cart);

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
        colorAdapter = new ArrayAdapter<>(ProductDetailActivity.this, R.layout.support_simple_spinner_dropdown_item,
                product.getProductColors().toArray(new String[0]));
        productColor.setAdapter(colorAdapter);

        ArrayAdapter<String> sizeAdapter;
        sizeAdapter = new ArrayAdapter<>(ProductDetailActivity.this,
                R.layout.support_simple_spinner_dropdown_item, product.getProductSizes().toArray(new String[1]));
        productSize.setAdapter(sizeAdapter);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    public void addComment(){
        final EditText commentTextView = new EditText(ProductDetailActivity.this);
        commentTextView.setSelectAllOnFocus(true);
        commentTextView.setHint("Write here");

        final AlertDialog.Builder commentDialog = new AlertDialog.Builder(ProductDetailActivity.this);

        commentDialog.setView(commentTextView);
        commentDialog.setTitle("Your Comment");

        commentDialog.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String commentMessage = commentTextView.getText().toString();
                String commentId = db.push().getKey();
                String commenterId = fbUser.getUid();

                SharedPreferences sp = getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
                String commenterName = sp.getString("CURRENT_USER_NAME", commenterId);

//                Review comments = new Review(commentId, commenterName, commentMessage, commenterId);
//                db.child("comments").child(product.getProductId()).child(commentId).setValue(comments);
                loadComments();
            }
        });

        commentDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        commentDialog.show();
    }

    public void addToCart(){
        SharedPreferences s = getSharedPreferences(getString(R.string.CART), MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        HashSet<String> cartItems = (HashSet<String>) s.getStringSet(getString(R.string.CART_ITEMS), new HashSet<String>());
        cartItems.add(product.getProductName());
        editor.clear();
        editor.putStringSet(getString(R.string.CART_ITEMS), cartItems).commit();
        Toast.makeText(ProductDetailActivity.this, "Item "+product.getProductName()+" added to cart",
                Toast.LENGTH_SHORT).show();
    }

    public void loadComments(){
        final ArrayList<Review> comments = new ArrayList<Review>();
        db.child("comments").child(product.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> commentTexts = new ArrayList<String>();
                for(DataSnapshot commentsResult : dataSnapshot.getChildren()){
                    Review comment = commentsResult.getValue(Review.class);
                    comments.add(comment);
                }
                CommentsAdapter myCommentsAdapter = new CommentsAdapter(getApplicationContext(), comments);
                productComments.setAdapter(myCommentsAdapter);
                productComments.setExpanded(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initNavigationDrawer() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_all_categories:
                        //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_faq:
                        //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_about:
                        //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.nav_header_text);
        userName.setText("TODO userName");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
