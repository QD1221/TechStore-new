package com.example.techstore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.techstore.R;
import com.example.techstore.adapter.DienthoaiAdapter;
import com.example.techstore.adapter.LaptopAdapter;
import com.example.techstore.model.Sanpham;
import com.example.techstore.ultil.CheckConnection;
import com.example.techstore.ultil.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LaptopActivity extends AppCompatActivity {

    Toolbar tbLaptop;
    ListView lvLaptop;
    FloatingActionButton btGiohang;
    LaptopAdapter laptopAdapter;
    ArrayList<Sanpham> mangLaptop;
    int idLaptop = 0;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    boolean limitdata = false;
    LaptopActivity.mHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        tbLaptop = findViewById(R.id.tbLaptop);
        lvLaptop = findViewById(R.id.lvLaptop);
        btGiohang = findViewById(R.id.btGiohang);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            ActionToolbar();
            GetIDLoaisp();
            GetData(page);
            LoadMoreData();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại Internet");
            finish();
        }
        mangLaptop = new ArrayList<>();
        laptopAdapter = new LaptopAdapter(getApplicationContext(),mangLaptop);
        lvLaptop.setAdapter(laptopAdapter);
        mHandler = new mHandler();
        Giohang();


    }

    public void Giohang(){
        btGiohang.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),Giohang.class);
            startActivity(intent);
        });
    }


    private void LoadMoreData() {
        lvLaptop.setOnItemClickListener((parent, view, i, l) -> {
            Intent intent = new Intent(getApplicationContext(),Chitietsp.class);
            intent.putExtra("thongtinsanpham", mangLaptop.get(i));
            startActivity(intent);
        });
        lvLaptop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount !=0 && isLoading == false &&limitdata == false){
                    isLoading = true;
                    LaptopActivity.ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdandienthoai + Page;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, response -> {
            int id;
            String tenLaptop;
            int giaLaptop;
            String hinhanhLaptop;
            String mota;
            int idLaptop;
            if (response != null && response.length() !=2){
                lvLaptop.removeFooterView(footerview);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        tenLaptop = jsonObject.getString("tensp");
                        giaLaptop = jsonObject.getInt("giasp");
                        hinhanhLaptop = jsonObject.getString("hinhanhsp");
                        mota = jsonObject.getString("motasp");
                        idLaptop = jsonObject.getInt("idsanpham");
                        mangLaptop.add(new Sanpham(id, tenLaptop,giaLaptop,hinhanhLaptop,mota,idLaptop));
                        laptopAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                limitdata = true;
                lvLaptop.removeFooterView(footerview);
                CheckConnection.ShowToast_Short(getApplicationContext(), "Đã hết dữ liệu");

            }

        }, error -> {

        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> param = new HashMap<>();
                param.put("idsanpham",String.valueOf(idLaptop));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar(){
        tbLaptop.setNavigationIcon(R.drawable.ic_back);
        tbLaptop.setNavigationOnClickListener(v -> finish());
    }

    private void GetIDLoaisp(){
        idLaptop = getIntent().getIntExtra("idloaisanpham",-1);
    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvLaptop.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}