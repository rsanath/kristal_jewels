package com.talenttakeaways.kristaljewels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.talenttakeaways.kristaljewels.beans.Comments;
import com.talenttakeaways.kristaljewels.beans.Product;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    NestedScrollView parent;

    FirebaseUser fbUser;
    DatabaseReference db;

    TextView productName, productPrice, productDescription;
    EditText commentBox;
    ImageView productImage, commentButton;
    LinearLayout reviewSection;
    ExpandableHeightListView productComments;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        product = Parcels.unwrap(intent.getParcelableExtra("product"));
        db = FirebaseDatabase.getInstance().getReference();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(product.getProductName());
        setSupportActionBar(toolbar);
        setView();

    }

    public void setView(){
        setComments();
        productImage = (ImageView) findViewById(R.id.product_cover);
        productName = (TextView) findViewById(R.id.product_name);
        productPrice = (TextView) findViewById(R.id.product_price);
        productDescription = (TextView) findViewById(R.id.product_description);
        commentBox = (EditText) findViewById(R.id.comment_box);
        commentButton = (ImageView) findViewById(R.id.comment_button);
        productComments = (ExpandableHeightListView) findViewById(R.id.product_comments);
        reviewSection = (LinearLayout) findViewById(R.id.review_section);

        // Set all values
        if(product.getProductImages() != null){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Glide.with(this).load(product.getProductImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .override(width, width).into(productImage);
        }
        productName.setText(product.getProductName());
        productPrice.setText("Rs. "+product.getProductPrice());
        productDescription.setText(product.getProductDescription());

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentBox.getText().toString().trim();
                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(getApplicationContext(), "Review can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //publishReview();
                Toast.makeText(getApplicationContext(), comment, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setComments(){
        final ArrayList<Comments> comments = new ArrayList<Comments>();
        db.child("comments").child(product.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> commentTexts = new ArrayList<String>();
                for(DataSnapshot commentsResult : dataSnapshot.getChildren()){
                    Comments comment = commentsResult.getValue(Comments.class);
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
}
