package com.example.techstore.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techstore.R;
import com.example.techstore.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LaptopAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arrayLaptop;

    public LaptopAdapter(Context context, ArrayList<Sanpham> arrayLaptop) {
        this.context = context;
        this.arrayLaptop = arrayLaptop;
    }

    @Override
    public int getCount() {
        return arrayLaptop.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayLaptop.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public static class ViewHolder{
        public TextView tvLaptop,tvGialaptop,tvMotalaptop;
        public ImageView ivLaptop;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LaptopAdapter.ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_laptop,null);
            viewHolder.tvLaptop = view.findViewById(R.id.tvLaptop);
            viewHolder.tvGialaptop= view.findViewById(R.id.tvGialaptop);
            viewHolder.tvMotalaptop = view.findViewById(R.id.tvMotalaptop);
            viewHolder.ivLaptop = view.findViewById(R.id.ivLaptop);
            view.setTag(viewHolder);



        }
        else {
            viewHolder = (LaptopAdapter.ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.tvLaptop.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvGialaptop.setText(decimalFormat.format(sanpham.getGiasanpham())+"₫");
        viewHolder.tvMotalaptop.setMaxLines(2);
        viewHolder.tvMotalaptop.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.tvMotalaptop.setText(sanpham.getMotasanpham());
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimg)
                .error(R.drawable.error)
                .into(viewHolder.ivLaptop);
        return view;
    }
}
