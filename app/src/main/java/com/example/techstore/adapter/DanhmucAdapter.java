package com.example.techstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techstore.R;
import com.example.techstore.model.DanhmucSanpham;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DanhmucAdapter extends BaseAdapter {
    ArrayList<DanhmucSanpham> arrayListloaisp;
    Context context;

    public DanhmucAdapter(ArrayList<DanhmucSanpham> arrayListloaisp, Context context) {
        this.arrayListloaisp = arrayListloaisp;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayListloaisp.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListloaisp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class  ViewHolder{
        TextView tvtenloaisp;
        ImageView ivloaisp;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_listview_loaisp, null);
            viewHolder.tvtenloaisp = view.findViewById(R.id.tvloaisp);
            viewHolder.ivloaisp = view.findViewById(R.id.ivloaisp);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DanhmucSanpham danhmucSanpham = (DanhmucSanpham) getItem(i);
        viewHolder.tvtenloaisp.setText(danhmucSanpham.getTenloaisp());
        Picasso.get()
                .load(danhmucSanpham.getHinhanhloaisp())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error2)
                .fit()
                .into(viewHolder.ivloaisp);

        return view;
    }
}
