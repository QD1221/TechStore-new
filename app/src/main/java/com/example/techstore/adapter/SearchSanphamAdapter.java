package com.example.techstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;
import com.example.techstore.activity.Chitietsp;
import com.example.techstore.model.Sanpham;
import com.example.techstore.ultil.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchSanphamAdapter extends RecyclerView.Adapter<SearchSanphamAdapter.ItemHolder> {
    Context context;
    ArrayList<Sanpham> mangsanpham;


    public SearchSanphamAdapter(Context context, ArrayList<Sanpham> mangsanpham) {
        this.context = context;
        this.mangsanpham = mangsanpham;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_tim_kiem_san_pham, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Sanpham sanpham = mangsanpham.get(position);
        holder.tvsearchsanpham.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvsearchgia.setText(decimalFormat.format(sanpham.getGiasanpham())+"₫");
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimg)
                .error(R.drawable.error)
                .into(holder.ivsearchsanpham);
    }

    @Override
    public int getItemCount() {
        return mangsanpham.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView ivsearchsanpham;
        public TextView tvsearchsanpham, tvsearchgia;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ivsearchsanpham = itemView.findViewById(R.id.ivsearchsanpham);
            tvsearchsanpham = itemView.findViewById(R.id.tvsearchsanpham);
            tvsearchgia = itemView.findViewById(R.id.tvsearchgia);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Chitietsp.class);
                    intent.putExtra("thongtinsanpham",mangsanpham.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}

