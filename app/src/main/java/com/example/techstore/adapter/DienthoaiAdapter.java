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
import com.example.techstore.activity.DienThoaiActivity;
import com.example.techstore.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DienthoaiAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arrayDienthoai;

    public DienthoaiAdapter(Context context, ArrayList<Sanpham> arrayDienthoai) {
        this.context = context;
        this.arrayDienthoai = arrayDienthoai;
    }

    @Override
    public int getCount() {
        return arrayDienthoai.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayDienthoai.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        public TextView tvDienthoai,tvGiadienthoai,tvMotadienthoai;
        public ImageView ivDienthoai;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_dienthoai,null);
            viewHolder.tvDienthoai = view.findViewById(R.id.tvDienthoai);
            viewHolder.tvGiadienthoai= view.findViewById(R.id.tvGiadienthoai);
            viewHolder.tvMotadienthoai = view.findViewById(R.id.tvMotadienthoai);
            viewHolder.ivDienthoai = view.findViewById(R.id.ivDienthoai);
            view.setTag(viewHolder);



        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.tvDienthoai.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvGiadienthoai.setText(decimalFormat.format(sanpham.getGiasanpham())+"₫");
        viewHolder.tvMotadienthoai.setMaxLines(2);
        viewHolder.tvMotadienthoai.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.tvMotadienthoai.setText(sanpham.getMotasanpham());
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimg)
                .error(R.drawable.error)
                .into(viewHolder.ivDienthoai);
        return view;
    }


}
