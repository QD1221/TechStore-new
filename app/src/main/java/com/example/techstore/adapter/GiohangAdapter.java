package com.example.techstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techstore.R;
import com.example.techstore.activity.MainActivity;
import com.example.techstore.model.Giohang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GiohangAdapter extends BaseAdapter {
    Context context;
    ArrayList<Giohang> manggiohang;
    LayoutInflater layoutInflater;

    public GiohangAdapter(Context context, ArrayList<Giohang> manggiohang) {
        this.context = context;
        this.manggiohang = manggiohang;
        this.layoutInflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return manggiohang== null ? 0 : manggiohang.size();
    }

    @Override
    public Object getItem(int position) {
        return manggiohang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        public TextView tvTengiohang,tvGiagiohang;
        public ImageView ivGiohang;
        public Button btTru, btGiatri, btCong;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_giohang,null);
            viewHolder.tvTengiohang = convertView.findViewById(R.id.tvTengiohang);
            viewHolder.tvGiagiohang = convertView.findViewById(R.id.tvGiagiohang);
            viewHolder.ivGiohang = convertView.findViewById(R.id.ivGiohang);
            viewHolder.btTru = convertView.findViewById(R.id.btTru);
            viewHolder.btGiatri = convertView.findViewById(R.id.btGiatri);
            viewHolder.btCong = convertView.findViewById(R.id.btCong);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Giohang giohang = (Giohang) getItem(position);
        viewHolder.tvTengiohang.setText(giohang.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvGiagiohang.setText(decimalFormat.format(giohang.getGiasp())+"₫");
        Picasso.get().load(giohang.getHinhsp())
                .placeholder(R.drawable.noimg)
                .error(R.drawable.error)
                .into(viewHolder.ivGiohang);

        viewHolder.btGiatri.setText(giohang.getSoluongsp() + "");
        int sl = Integer.parseInt(viewHolder.btGiatri.getText().toString());
        if (sl>=10){
            viewHolder.btCong.setVisibility(View.INVISIBLE);
            viewHolder.btTru.setVisibility(View.VISIBLE);
        }else if (sl<=1){
            viewHolder.btTru.setVisibility(View.INVISIBLE);
        }else if (sl>=1){
            viewHolder.btTru.setVisibility(View.VISIBLE);
            viewHolder.btCong.setVisibility(View.VISIBLE);
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slmoinhat = Integer.parseInt(finalViewHolder.btGiatri.getText().toString()) +1;
                int slhientai = MainActivity.manggiohang.get(position).getSoluongsp();
                long giahientai = MainActivity.manggiohang.get(position).getGiasp();
                MainActivity.manggiohang.get(position).setSoluongsp(slmoinhat);
                long giamoinhat= (giahientai * slmoinhat)/slhientai;
                MainActivity.manggiohang.get(position).setGiasp(giamoinhat);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.tvGiagiohang.setText(decimalFormat.format(giamoinhat)+"₫");
                com.example.techstore.activity.Giohang.EventUtil();
                if (slmoinhat > 9){
                    finalViewHolder.btCong.setVisibility(View.INVISIBLE);
                    finalViewHolder.btTru.setVisibility(View.VISIBLE);
                    finalViewHolder.btGiatri.setText(String.valueOf(slmoinhat));
                }else{
                    finalViewHolder.btTru.setVisibility(View.VISIBLE);
                    finalViewHolder.btCong.setVisibility(View.VISIBLE);
                    finalViewHolder.btGiatri.setText(String.valueOf(slmoinhat));
                }
            }
        });
        viewHolder.btTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slmoinhat = Integer.parseInt(finalViewHolder.btGiatri.getText().toString()) - 1;
                int slhientai = MainActivity.manggiohang.get(position).getSoluongsp();
                long giahientai = MainActivity.manggiohang.get(position).getGiasp();
                MainActivity.manggiohang.get(position).setSoluongsp(slmoinhat);
                long giamoinhat= (giahientai * slmoinhat)/slhientai;
                MainActivity.manggiohang.get(position).setGiasp(giamoinhat);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.tvGiagiohang.setText(decimalFormat.format(giamoinhat)+"₫");
                com.example.techstore.activity.Giohang.EventUtil();
                if (slmoinhat <2){
                    finalViewHolder.btTru.setVisibility(View.INVISIBLE);
                    finalViewHolder.btCong.setVisibility(View.VISIBLE);
                    finalViewHolder.btGiatri.setText(String.valueOf(slmoinhat));
                }else{
                    finalViewHolder.btTru.setVisibility(View.VISIBLE);
                    finalViewHolder.btCong.setVisibility(View.VISIBLE);
                    finalViewHolder.btGiatri.setText(String.valueOf(slmoinhat));
                }
            }
        });
        return convertView;
    }
}
