package com.example.techstore.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.techstore.R;
import com.example.techstore.activity.GiohangActivity;
import com.example.techstore.activity.MainActivity;
import com.example.techstore.model.Giohang;
import com.example.techstore.model.Sanpham;
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
        public TextView tvTengiohang,tvGiagiohang, btGiatri, tvThongbao, tvTongtien;
        public ImageView ivGiohang;
        public Button btTru, btCong, btXoa;
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
            viewHolder.tvThongbao = convertView.findViewById(R.id.tvThongbao);
            viewHolder.tvTongtien = convertView.findViewById(R.id.tvTongtien);
            viewHolder.ivGiohang = convertView.findViewById(R.id.ivGiohang);
            viewHolder.btTru = convertView.findViewById(R.id.btTru);
            viewHolder.btGiatri = convertView.findViewById(R.id.btGiatri);
            viewHolder.btCong = convertView.findViewById(R.id.btCong);
            viewHolder.btXoa = convertView.findViewById(R.id.btxoa);

            viewHolder.btXoa.setTag(position);
            viewHolder.btXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Xác nhận xóa sản phẩm");
                    builder.setIcon(ContextCompat.getDrawable(context,R.drawable.warning2));
                    builder.setMessage("Bạn có chắc muốn xóa sản phẩm này?");
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            manggiohang.remove(position);
                            notifyDataSetChanged();
                            ((Activity)context).recreate();
                        }
                    });

                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notifyDataSetChanged();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
            });
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
                GiohangActivity.EventUtil();
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
                GiohangActivity.EventUtil();
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
