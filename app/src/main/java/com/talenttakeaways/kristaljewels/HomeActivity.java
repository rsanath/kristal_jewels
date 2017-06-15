package com.talenttakeaways.kristaljewels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by sanath on 09/06/17.
 */

public class HomeActivity extends AppCompatActivity {
    Button logoutButton;
    FirebaseAuth mAuth;
//    FirebaseDatabase db;
    DatabaseReference dbRef;

    ImageView sectionImages, categoryImage1, categoryImage2, categoryImage3, categoryImage4;
    ProgressDialog pd;

    //Stuff for the image slide (PageViewer)
    private static ViewPager mPager;
    private static int currentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        checkLoginStatus();
        initSlider();

        categoryImage1 = (ImageView) findViewById(R.id.section_image_1);
        categoryImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("category", "necklaces");
                startActivity(intent);
            }
        });
        categoryImage2 = (ImageView) findViewById(R.id.section_image_2);
        categoryImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("category", "bangles");
                startActivity(intent);
            }
        });

        categoryImage3 = (ImageView) findViewById(R.id.section_image_3);
        categoryImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("category", "rings");
                startActivity(intent);
            }
        });

        categoryImage4 = (ImageView) findViewById(R.id.section_image_4);
        categoryImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("category", "earrings");
                startActivity(intent);
            }
        });






        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        sectionImages = (ImageView) findViewById(R.id.section_image_1);
        Glide.with(this).load("https://goo.gl/8lwO3K")
                .override(400, 400).into(sectionImages);

        sectionImages = (ImageView) findViewById(R.id.section_image_2);
        Glide.with(this).load("https://goo.gl/5xfZSW")
                .override(400, 400).into(sectionImages);

        sectionImages = (ImageView) findViewById(R.id.section_image_3);
        Glide.with(this).load("http://www.pngadgilandsons.com/wp-content/uploads/Bangle-131.jpg")
                .override(400, 400).into(sectionImages);

        sectionImages = (ImageView) findViewById(R.id.section_image_4);
        Glide.with(this).load("http://www.janhaviartjewellery.com/wp-content/gallery/fashion-earrings/" +
                "indian-traditional-earrings-online-shopping.jpg")
                .override(400, 400).into(sectionImages);

    }

    public void checkLoginStatus(){
        if(mAuth.getCurrentUser() == null){
            finish();
            showToast("You must login first!");
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
    }

    public void showToast(String text){
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
        mPager.setAdapter(new MyAdapter(HomeActivity.this, imagesArray));
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

}

class MyAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    private String[] images;

    public MyAdapter(Context context, String[] images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myImageLayout = inflater.inflate(R.layout.slide_view_image, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);

        Glide.with(context)
                .load(images[position])
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(myImage);

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
