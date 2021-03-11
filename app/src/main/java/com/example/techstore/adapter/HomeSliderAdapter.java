package com.example.techstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.techstore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> mangquangcao;

    public HomeSliderAdapter(Context context) {
        this.context = context;
    }

    public HomeSliderAdapter(Context context, ArrayList<String> mangquangcao) {
        this.context = context;
        this.mangquangcao = mangquangcao;
    }

    @Override
    public int getCount() {
        mangquangcao = new ArrayList<>();
        mangquangcao.add("https://cdn.tgdd.vn/2021/02/banner/S21-800-300-800x300-7.png");
        mangquangcao.add("https://cdn.tgdd.vn/2021/03/banner/reno5-800-300-800x300-1.png");
        mangquangcao.add("https://cdn.tgdd.vn/2021/02/banner/800-300-800x300-20.png");
        mangquangcao.add("https://cdn.tgdd.vn/2021/03/banner/800-300-800x300-4.png");
        return mangquangcao.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_home_slider, null);
        ImageView imageView = view.findViewById(R.id.imageView);

        Picasso.get().load(mangquangcao.get(position)).into(imageView);


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}