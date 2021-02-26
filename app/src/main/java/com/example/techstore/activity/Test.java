package com.example.techstore.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.techstore.R;
import com.example.techstore.model.Sanpham;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class Test extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ImageView ivChitiet;
    TextView tvTen,tvGia, tvChitiet, giatri;
    ImageView cong, tru;
    Button btdatmua;

    int id;
    String Tenchitiet;
    int Giachitiet;
    String Hinhanhchitiet;
    String Motachitiet;
    int idsanpham;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Này anh iu em muốn uống tà tưa");
        collapsingToolbarLayout.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_CENTER);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

        toolbar = findViewById(R.id.toolbartest);
//        ivChitiet = findViewById(R.id.ivtest);
//        tvTen = findViewById(R.id.tvtentest);
//        tvGia = findViewById(R.id.tvgiatest);
//        tvChitiet = findViewById(R.id.tvchitiettest);
//        cong = findViewById(R.id.bcongtest);
//        tru = findViewById(R.id.btrutest);
//        giatri = findViewById(R.id.bgiatritest);
//        btdatmua = findViewById(R.id.bdatmuatest);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetInformation();
    }

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
        tvChitiet.setText(Motachitiet);
        Picasso.get().load(Hinhanhchitiet)
                .placeholder(R.drawable.noimg)
                .error(R.drawable.error)
                .into(ivChitiet);
    }
}
