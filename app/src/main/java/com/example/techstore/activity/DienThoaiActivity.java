package com.example.techstore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.techstore.R;
import com.example.techstore.adapter.DienthoaiAdapter;
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

import static com.example.techstore.R.drawable.*;

public class DienThoaiActivity extends AppCompatActivity {

    Toolbar tbDienthoai;
    ListView lvDienthoai;
    FloatingActionButton btGiohang;
    DienthoaiAdapter dienthoaiAdapter;
    ArrayList<Sanpham> mangDienthoai;
    int idDienthoai = 0;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    boolean limitdata = false;
    mHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        tbDienthoai = findViewById(R.id.tbDienthoai);
        lvDienthoai = findViewById(R.id.lvDienthoai);
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
        mangDienthoai = new ArrayList<>();
        dienthoaiAdapter = new DienthoaiAdapter(getApplicationContext(),mangDienthoai);
        lvDienthoai.setAdapter(dienthoaiAdapter);
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
        lvDienthoai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Chitietsp.class);
                intent.putExtra("thongtinsanpham", mangDienthoai.get(i));
                startActivity(intent);
            }
        });
        lvDienthoai.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount !=0 && isLoading == false &&limitdata == false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
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
            String tenDienthoai;
            int giaDienthoai;
            String hinhanhDienthoai;
            String mota;
            int idDienthoai;
            if (response != null && response.length() !=2){
                lvDienthoai.removeFooterView(footerview);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        tenDienthoai = jsonObject.getString("tensp");
                        giaDienthoai = jsonObject.getInt("giasp");
                        hinhanhDienthoai = jsonObject.getString("hinhanhsp");
                        mota = jsonObject.getString("motasp");
                        idDienthoai = jsonObject.getInt("idsanpham");
                        mangDienthoai.add(new Sanpham(id, tenDienthoai,giaDienthoai,hinhanhDienthoai,mota,idDienthoai));

                    }
                    dienthoaiAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                limitdata = true;
                lvDienthoai.removeFooterView(footerview);
                CheckConnection.ShowToast_Short(getApplicationContext(), "Đã hết dữ liệu");

            }

        }, error -> {

        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> param = new HashMap<>();
                param.put("idsanpham",String.valueOf(idDienthoai));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar(){
        tbDienthoai.setNavigationIcon(ic_back);
        tbDienthoai.setNavigationOnClickListener(v -> finish());

    }

    private void GetIDLoaisp(){
        idDienthoai = getIntent().getIntExtra("idloaisanpham",-1);
    }

    @SuppressLint("HandlerLeak")
    public class mHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvDienthoai.addFooterView(footerview);
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




