package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.techstore.R;
import com.example.techstore.model.Giohang;
import com.example.techstore.model.Sanpham;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class Chitietsp extends AppCompatActivity {

    Toolbar tbChitiet;
    ImageView ivChitiet;
    TextView tvTen,tvGia, tvMota;
    Spinner spinner;
    Button btdatmua;
    Button btTruCt, btGiatriCt, btCongCt;

    FloatingActionButton btGiohang;

    int id;
    String Tenchitiet;
    int Giachitiet;
    String Hinhanhchitiet;
    String Motachitiet;
    int idsanpham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsp);
        tbChitiet = findViewById(R.id.tbChitietsp);
        ivChitiet = findViewById(R.id.ivChitietsp);
        tvTen = findViewById(R.id.tvtenchitietsp);
        tvGia = findViewById(R.id.tvgiachitietsp);
        tvMota = findViewById(R.id.tvmotachitietsp);
        btdatmua = findViewById(R.id.btdatmua);
        btGiohang = findViewById(R.id.btGiohang);
        btTruCt = findViewById(R.id.btTruCt);
        btGiatriCt = findViewById(R.id.btGiatriCt);
        btCongCt = findViewById(R.id.btCongCt);



        ActionToolbar();
        GetInformation();
//        CatchEventSpinner();
        EventButton();
        Giohang();

        btCongCt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slmoinhat = Integer.parseInt(btGiatriCt.getText().toString()) +1;

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
                int slmoinhat = Integer.parseInt(btGiatriCt.getText().toString()) - 1;


                if (slmoinhat <2){
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
        btGiohang.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), com.example.techstore.activity.Giohang.class);
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
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long Giamoi = soluong * Giachitiet;
                    MainActivity.manggiohang.add(new Giohang(id,Tenchitiet,Giamoi,Hinhanhchitiet,soluong));
                }
            }else {
                int soluong = Integer.parseInt(btGiatriCt.getText().toString());
                long Giamoi = soluong * Giachitiet;
                MainActivity.manggiohang.add(new Giohang(id,Tenchitiet,Giamoi,Hinhanhchitiet,soluong));
            }
            Intent intent = new Intent(getApplicationContext(), com.example.techstore.activity.Giohang.class);
            startActivity(intent);
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

    private void ActionToolbar(){
        tbChitiet.setNavigationIcon(R.drawable.ic_back);
        tbChitiet.setNavigationOnClickListener(v -> finish());
    }

}