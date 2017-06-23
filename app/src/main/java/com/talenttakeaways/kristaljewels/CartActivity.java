package com.talenttakeaways.kristaljewels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;

/**
 * Created by sanath on 23/06/17.
 */

public class CartActivity extends AppCompatActivity{


    //database objects
    FirebaseUser fbUser;
    DatabaseReference dbRef;

    //toolbar and navigation drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    //cart set
    HashSet<String> cartItems;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        initNavigationDrawer();

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        sp = getSharedPreferences(getString(R.string.CART), MODE_PRIVATE);
        cartItems = (HashSet) sp.getStringSet(getString(R.string.CART_ITEMS), null);

        final TextView text = (TextView) findViewById(R.id.text_text);

        if(cartItems != null) {
            text.setText(cartItems.toString());
        }

        Button clearCart = (Button) findViewById(R.id.clear_cart_button);
        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear().commit();
                text.setText("");
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
