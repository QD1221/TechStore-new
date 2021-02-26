package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.techstore.R;
import com.example.techstore.adapter.GiohangAdapter;
import com.example.techstore.ultil.CheckConnection;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

import static com.example.techstore.activity.MainActivity.manggiohang;

public class Giohang extends AppCompatActivity {
    ListView lvGiohang;
    TextView tvThongbao;
    static TextView tvTongtien;
    Button btThanhtoan,btTieptucmua;
    Toolbar tbGiohang;
    GiohangAdapter giohangAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        lvGiohang = findViewById(R.id.lvGiohang);
        tvThongbao = findViewById(R.id.tvThongbao);
        tvTongtien = findViewById(R.id.tvTongtien);
        btThanhtoan = findViewById(R.id.btThanhtoangiohang);
        btTieptucmua = findViewById(R.id.btTieptucmuahang);
        tbGiohang = findViewById(R.id.tbGiohang);
        giohangAdapter= new GiohangAdapter(Giohang.this, manggiohang);
        lvGiohang.setAdapter(giohangAdapter);

        ActionToolbar();
        CheckData();
        EventUtil();
        CatchOnItemListView();
        EventButton();

    }

    private void EventButton() {
        btTieptucmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        btThanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manggiohang.size() >0){
                    Intent intent = new Intent(getApplicationContext(), ThongtinKH.class);
                    startActivity(intent);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Chưa có sản phẩm để thanh toán");
                }
            }
        });
    }

    public void CatchOnItemListView() {
        lvGiohang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Giohang.this);
                builder.setTitle("Xác nhận xóa sản phẩm");
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (manggiohang.size() <=0){
                            tvThongbao.setVisibility(View.VISIBLE);
                        }else {
                            manggiohang.remove(position);
                            giohangAdapter.notifyDataSetChanged();
                            EventUtil();
                            if (manggiohang.size()<=0){
                                tvThongbao.setVisibility(View.VISIBLE);
                            }else {
                                tvThongbao.setVisibility(View.INVISIBLE);
                                giohangAdapter.notifyDataSetChanged();
                                EventUtil();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        giohangAdapter.notifyDataSetChanged();
                        EventUtil();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public static void EventUtil() {
        long tongtien = 0;
        if (manggiohang != null) {
            manggiohang.size();
        for (int i = 0; i< manggiohang.size(); i++){
            tongtien+= manggiohang.get(i).getGiasp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTongtien.setText(decimalFormat.format(tongtien) + "₫");
        }
    }

    private void CheckData() {
        if (manggiohang != null) {
            manggiohang.size();

        if (manggiohang.size() <= 0){
            giohangAdapter.notifyDataSetChanged();
            tvThongbao.setVisibility(View.VISIBLE);
            lvGiohang.setVisibility(View.INVISIBLE);
        } else {
            giohangAdapter.notifyDataSetChanged();
            tvThongbao.setVisibility(View.INVISIBLE);
            lvGiohang.setVisibility(View.VISIBLE);
        }
        }
    }

    private void ActionToolbar(){
        tbGiohang.setNavigationIcon(R.drawable.ic_back);
        tbGiohang.setNavigationOnClickListener(v -> finish());
    }
}