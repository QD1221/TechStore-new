package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techstore.R;
import com.example.techstore.model.Giohang;
import com.example.techstore.model.Sanpham;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChitietSanphamActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ImageView ivChitiet;
    TextView tvTen,tvGia, tvMota, btGiatriCt;
    Button btdatmua;
    Button btTruCt, btCongCt;

    FrameLayout flcart;
    TextView tvcart;

    int id;
    String Tenchitiet;
    int Giachitiet;
    String Hinhanhchitiet;
    String Motachitiet;
    int idsanpham;
    int slmoinhat = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsp);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Chi tiết sản phẩm");
        collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_CENTER);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

        toolbar = findViewById(R.id.toolbartest);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivChitiet = findViewById(R.id.ivChitietsp);
        tvTen = findViewById(R.id.tvtenchitietsp);
        tvGia = findViewById(R.id.tvgiachitietsp);
        tvMota = findViewById(R.id.tvmotachitietsp);
        btdatmua = findViewById(R.id.btdatmua);

        flcart = findViewById(R.id.flcart);
        tvcart = findViewById(R.id.tvcart);

        btTruCt = findViewById(R.id.btTruCt);
        btGiatriCt = findViewById(R.id.btGiatriCt);
        btCongCt = findViewById(R.id.btCongCt);

        if (MainActivity.manggiohang !=null){

        }else {
            MainActivity.manggiohang = new ArrayList<>();
        }

        GetInformation();
//        CatchEventSpinner();
        EventButton();
        Giohang();

        btCongCt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slmoinhat = Integer.parseInt(btGiatriCt.getText().toString()) +1;

                if (slmoinhat > 9){
                    btCongCt.setVisibility(View.INVISIBLE);
                    btTruCt.setVisibility(View.VISIBLE);
                    btGiatriCt.setText(String.valueOf(slmoinhat));
                }else{
                    btTruCt.setVisibility(View.VISIBLE);
                    btCongCt.setVisibility(View.VISIBLE);
                    btGiatriCt.setText(String.valueOf(slmoinhat));

                }
            }
        });

        btTruCt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slmoinhat > 1){
                    slmoinhat = Integer.parseInt(btGiatriCt.getText().toString()) - 1;
                }


                if (slmoinhat <= 1){
                    btTruCt.setVisibility(View.INVISIBLE);
                    btCongCt.setVisibility(View.VISIBLE);
                    btGiatriCt.setText(String.valueOf(slmoinhat));
                }else{
                    btTruCt.setVisibility(View.VISIBLE);
                    btCongCt.setVisibility(View.VISIBLE);
                    btGiatriCt.setText(String.valueOf(slmoinhat));

                }
            }
        });


    }


    public void Giohang(){
        if (MainActivity.manggiohang.size() != 0){
            tvcart.setText(String.valueOf(MainActivity.manggiohang.size()));
        }else {
            tvcart.setText("0");
        }
        flcart.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GiohangActivity.class);
            startActivity(intent);
        });
    }

    public void EventButton() {
        btdatmua.setOnClickListener(v -> {
            if (MainActivity.manggiohang.size()>0){
                int sl= Integer.parseInt(btGiatriCt.getText().toString());
                btGiatriCt.setText(String.valueOf(sl));
                if (sl>=10){
                    btCongCt.setVisibility(View.INVISIBLE);
                    btTruCt.setVisibility(View.VISIBLE);
                }else if (sl<=1){
                    btTruCt.setVisibility(View.INVISIBLE);
                }else if (sl>=1){
                    btTruCt.setVisibility(View.VISIBLE);
                    btCongCt.setVisibility(View.VISIBLE);
                }

                boolean exists = false;
                for (int i=0; i<MainActivity.manggiohang.size(); i++){
                    if (MainActivity.manggiohang.get(i).getIdsp() == id){
                        MainActivity.manggiohang.get(i).setSoluongsp(MainActivity.manggiohang.get(i).getSoluongsp() + sl);
                        if (MainActivity.manggiohang.get(i).getSoluongsp()>=10){
                            MainActivity.manggiohang.get(i).setSoluongsp(10);
                        }
                        MainActivity.manggiohang.get(i).setGiasp(Giachitiet * MainActivity.manggiohang.get(i).getSoluongsp());
                        exists = true;
                    }
                }
                if (exists == false){
                    int soluong = Integer.parseInt(btGiatriCt.getText().toString());
                    long Giamoi = soluong * Giachitiet;
                    MainActivity.manggiohang.add(new Giohang(id,Tenchitiet,Giamoi,Hinhanhchitiet,soluong));
                }
            }else {
                int soluong = Integer.parseInt(btGiatriCt.getText().toString());
                long Giamoi = soluong * Giachitiet;
                MainActivity.manggiohang.add(new Giohang(id,Tenchitiet,Giamoi,Hinhanhchitiet,soluong));
            }
            showCustomDialog();
        });
    }

//    private void CatchEventSpinner() {
//        Integer[] soluong = new Integer[]{1,2,3,4,5,6,7,8,9,10};
//        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,soluong);
//        spinner.setAdapter(arrayAdapter);
//    }

    public void GetInformation() {

        Sanpham sanpham = (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");
        id = sanpham.getID();
        Tenchitiet = sanpham.getTensanpham();
        Giachitiet = sanpham.getGiasanpham();
        Hinhanhchitiet = sanpham.getHinhanhsanpham();
        Motachitiet = sanpham.getMotasanpham();
        idsanpham = sanpham.getIDSanpham();

        tvTen.setText(Tenchitiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvGia.setText(decimalFormat.format(Giachitiet) +"₫");
        tvMota.setText(Motachitiet);
        Picasso.get().load(Hinhanhchitiet)
                .placeholder(R.drawable.noimg)
                .error(R.drawable.error)
                .into(ivChitiet);
    }

    private void showCustomDialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(ChitietSanphamActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_giohang);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvdialog = dialog.findViewById(R.id.tvdialog);
        tvdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                recreate();
                dialog.dismiss();
            }
        });
        // Set dialog title

        dialog.show();

    }

}