package com.talenttakeaways.kristaljewels;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.talenttakeaways.kristaljewels.adapters.SliderAdapter;
import com.talenttakeaways.kristaljewels.others.CommonFunctions;
import com.talenttakeaways.kristaljewels.others.Constants;
import com.talenttakeaways.kristaljewels.ui.CartActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by sanath on 09/06/17.
 */

public class HomeActivity extends AppCompatActivity {
    Context context = this;

    FirebaseAuth mAuth;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.section_image_1) ImageView categoryImage1;
    @BindView(R.id.section_image_2) ImageView categoryImage2;
    @BindView(R.id.section_image_3) ImageView categoryImage3;
    @BindView(R.id.section_image_4) ImageView categoryImage4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        loadView();
    }

    public void loadView() {

        initSlider();
        CommonFunctions.initNavigationDrawer(this, toolbar, false);

        View.OnClickListener categoriedButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra(Constants.from, Constants.button);
                switch (id) {
                    case R.id.section_image_1:
                        intent.putExtra(Constants.category, Constants.necklace);
                        break;
                    case R.id.section_image_2:
                        intent.putExtra(Constants.category, Constants.bangle);
                        break;
                    case R.id.section_image_3:
                        intent.putExtra(Constants.category, Constants.ring);
                        break;
                    case R.id.section_image_4:
                        intent.putExtra(Constants.category, Constants.earring);
                        break;
                }
                startActivity(intent);
            }
        };
        categoryImage1.setOnClickListener(categoriedButtonListener);
        categoryImage2.setOnClickListener(categoriedButtonListener);
        categoryImage3.setOnClickListener(categoriedButtonListener);
        categoryImage4.setOnClickListener(categoriedButtonListener);
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

    private void initSlider() {
        final String[] imagesArray = {
                "https://assets.ritani.com/assets/116342003_poster-64977bad3a29b28c128c75601896a00c.jpg",
                "https://cdn-az.allevents.in/banners/f73bb79b1e350b456717949ff13d7412",
                "http://dalemku.com/x/2017/03/most-beautiful-diamond-necklace-world-dropssol-with-most-expensive-diamond-necklace.jpg"
        };

        //Stuff for the image slide (PageViewer)
        final ViewPager mPager = (ViewPager) findViewById(R.id.home_image_slide);
        mPager.setAdapter(new SliderAdapter(HomeActivity.this, imagesArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.home_circle_indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            int currentPage = 0;
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
        }, 3500, 3500);
    }
}

