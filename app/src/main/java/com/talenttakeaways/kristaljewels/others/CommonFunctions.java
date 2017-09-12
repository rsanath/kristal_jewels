package com.talenttakeaways.kristaljewels.others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.talenttakeaways.kristaljewels.HomeActivity;
import com.talenttakeaways.kristaljewels.LoginActivity;
import com.talenttakeaways.kristaljewels.ProductListActivity;
import com.talenttakeaways.kristaljewels.R;
import com.talenttakeaways.kristaljewels.models.CartItem;
import com.talenttakeaways.kristaljewels.models.User;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.talenttakeaways.kristaljewels.others.Constants.cartItems;
import static com.talenttakeaways.kristaljewels.others.Constants.currentUser;

/**
 * Created by sanath on 17/07/17.
 */

public class CommonFunctions {

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static MaterialDialog.Builder getLoadingDialog(Context context, String title, String content) {
       return new MaterialDialog.Builder(context)
               .title(title).content(content)
               .contentColor(Color.BLACK).cancelable(false)
               .autoDismiss(true).progress(true, 0);
    }

    public static MaterialDialog.Builder getLoadingDialog(Context context, int titleId, int contentId) {
        return new MaterialDialog.Builder(context)
                .title(titleId).content(contentId)
                .contentColor(Color.BLACK).cancelable(false)
                .autoDismiss(true).progress(true, 0);
    }

    public static MaterialDialog.Builder getDismissDialog(Context context, String title, String content) {
        return new MaterialDialog.Builder(context)
                .title(title).content(content)
                .contentColor(Color.BLACK).positiveText(R.string.ok);
    }

    public static MaterialDialog.Builder getDismissDialog(Context context, int titleId, int contentId) {
        return new MaterialDialog.Builder(context)
                .title(titleId).content(contentId)
                .contentColor(Color.BLACK).positiveText(R.string.ok);
    }

    public static void initNavigationDrawer(final Activity context, final Toolbar toolbar, final boolean backButton) {
        final DrawerLayout drawerLayout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) context.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (!backButton) {
                            drawerLayout.closeDrawers();
                        } else {
                            context.finish();
                            context.startActivity(new Intent(context, HomeActivity.class));
                        }
                        break;
                    case R.id.nav_Bangles:
                        Intent intent = new Intent(context, ProductListActivity.class);
                        intent.putExtra(Constants.from, Constants.button);
                        intent.putExtra(Constants.category, Constants.bangle);
                        context.startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_Necklace:
                        Intent intent1 = new Intent(context, ProductListActivity.class);
                        intent1.putExtra(Constants.from, Constants.button);
                        intent1.putExtra(Constants.category, Constants.necklace);
                        context.startActivity(intent1);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_Rings:
                        Intent intent2 = new Intent(context, ProductListActivity.class);
                        intent2.putExtra(Constants.from, Constants.button);
                        intent2.putExtra(Constants.category, Constants.ring);
                        context.startActivity(intent2);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_Earrings:
                        Intent intent3 = new Intent(context, ProductListActivity.class);
                        intent3.putExtra(Constants.from, Constants.button);
                        intent3.putExtra(Constants.category, Constants.earring);
                        context.startActivity(intent3);
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
                        logout(context);
                        context.finish();
                        context.startActivity(new Intent(context, LoginActivity.class));
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.nav_header_text);
        User user = CommonFunctions.getCurrentUser(context);
        userName.setText(user.getName());

        if (backButton) {
            toolbar.setNavigationIcon(R.drawable.arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.onBackPressed();
                }
            });
        } else {

            ActionBarDrawerToggle actionBarDrawerToggle =
                    new ActionBarDrawerToggle(context, drawerLayout, toolbar,
                            R.string.drawer_open, R.string.drawer_close) {

                        @Override
                        public void onDrawerClosed(View v) {
                            super.onDrawerClosed(v);
                        }

                        @Override
                        public void onDrawerOpened(View v) {
                            super.onDrawerOpened(v);
                        }
                    };
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }
    }

    public static ArrayList<CartItem> getCartItems(Context context){
        SharedPreferences sp = context.getSharedPreferences(Constants.cartItems, MODE_PRIVATE);

        String itemsStringList = sp.getString(cartItems, null);

        if (itemsStringList == null) {
            return new ArrayList<CartItem>();
        }
        ArrayList<CartItem> cartItems = new Gson().fromJson(itemsStringList, Constants.cartItemsType);
        return cartItems;
    }

    public static void setCartItems(Context context, ArrayList<CartItem> cartItems) {
        String itemsAsString = new Gson().toJson(cartItems);
        SharedPreferences sp = context.getSharedPreferences(Constants.cartItems, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(Constants.cartItems, itemsAsString).apply();
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();
        setCurrentUser(context, null);
    }


    public static User getCurrentUser(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences(Constants.currentUser, MODE_PRIVATE);
        String userAsString = mPrefs.getString(currentUser, null);
        User user = new Gson().fromJson(userAsString, User.class);
        return user;
    }

    public static void setCurrentUser(Context context, User user) {
        String userAsString = new Gson().toJson(user);
        SharedPreferences mPrefs = context.getSharedPreferences(Constants.currentUser, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(Constants.currentUser, userAsString).commit();
    }
}
