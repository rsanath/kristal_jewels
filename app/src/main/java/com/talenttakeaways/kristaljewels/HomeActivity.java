package com.talenttakeaways.kristaljewels;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.talenttakeaways.kristaljewels.adapters.SliderAdapter;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by sanath on 09/06/17.
 */

public class HomeActivity extends AppCompatActivity {

    //Stuff for the image slide (PageViewer)
    private static ViewPager mPager;
    private static int currentPage = 0;

    //FirebaseDatabase db;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    ImageView sectionImages, categoryImage1, categoryImage2, categoryImage3, categoryImage4;
    ProgressDialog pd;

    //toolbar and navigation drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        checkLoginStatus();
        initSlider();
        initNavigationDrawer();

        categoryImage1 = (ImageView) findViewById(R.id.section_image_1);
        categoryImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("from", "button");
                intent.putExtra("type", "category");
                intent.putExtra("category", "necklaces");
                startActivity(intent);
            }
        });
        categoryImage2 = (ImageView) findViewById(R.id.section_image_2);
        categoryImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("from", "button");
                intent.putExtra("type", "category");
                intent.putExtra("category", "bangles");
                startActivity(intent);
            }
        });

        categoryImage3 = (ImageView) findViewById(R.id.section_image_3);
        categoryImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("from", "button");
                intent.putExtra("type", "category");
                intent.putExtra("category", "rings");
                startActivity(intent);
            }
        });

        categoryImage4 = (ImageView) findViewById(R.id.section_image_4);
        categoryImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("from", "button");
                intent.putExtra("type", "category");
                intent.putExtra("category", "earrings");
                startActivity(intent);
            }
        });
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
                intent.putExtra("from", "search");
                intent.putExtra("search", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onStart() {
        super.onStart();

        sectionImages = (ImageView) findViewById(R.id.section_image_1);
        Glide.with(this).load("https://goo.gl/8lwO3K").diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .override(400, 400).into(sectionImages);

        sectionImages = (ImageView) findViewById(R.id.section_image_2);
        Glide.with(this).load("https://goo.gl/5xfZSW")
                .override(400, 400).into(sectionImages);

        sectionImages = (ImageView) findViewById(R.id.section_image_3);
        Glide.with(this).load("https://climg7.bluestone.com/f_jpg,c_scale,w_418,b_rgb:f0f0f0/giprod" +
                "uct/BISM0018B09-POSTER-7426.jpg")
                .override(400, 400).into(sectionImages);

        sectionImages = (ImageView) findViewById(R.id.section_image_4);
        Glide.with(this).load("http://www.dhresource.com/200x200s/f2-albu-g5-M00-AC-1B-rBVaI1kYM3uAJL-" +
                "DAA1vJR8cWdg356.jpg/huche-vintage-copper-earrings-the-shape-of.jpg")
                .override(400, 400).into(sectionImages);
    }

    public void checkLoginStatus() {
        if (mAuth.getCurrentUser() == null) {
            finish();
            showToast("You must login first!");
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void initSlider() {
        final String[] imagesArray = {
                "http://pondicherryinfo.in/main/files/2012/10/Jos-Alukkas-Pondicherry.jpg",
                "http://www.josalukkasonline.com/upload/color/jos-alukkas-Showroom-subha-mangalyam-weddin" +
                        "g-collection-work-bn4hhr.jpg",
                "http://www.josalukkasonline.com/upload/color/jos-alukkas-News-two-new-sh" +
                        "owrooms-opening-in-august-2015-0bxmt6.jpg"
        };

        mPager = (ViewPager) findViewById(R.id.home_image_slide);
        mPager.setAdapter(new SliderAdapter(HomeActivity.this, imagesArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.home_circle_indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imagesArray.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }

    public void initNavigationDrawer() {
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
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

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

