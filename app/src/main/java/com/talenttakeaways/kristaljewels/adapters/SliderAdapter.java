package com.talenttakeaways.kristaljewels.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.talenttakeaways.kristaljewels.R;

/**
 * Created by sanath on 17/06/17.
 */
public class SliderAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    private String[] images;

    public SliderAdapter(Context context, String[] images) {
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
        View myImageLayout = inflater.inflate(R.layout.image_slider, view, false);
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
