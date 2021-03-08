package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.techstore.R;
import com.example.techstore.adapter.LoaispAdapter;
import com.example.techstore.adapter.SanphamAdapter;
import com.example.techstore.model.Loaisp;
import com.example.techstore.model.Sanpham;
import com.example.techstore.ultil.CheckConnection;
import com.example.techstore.ultil.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar tbHome;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewHome;
    NavigationView navigationViewHome;
    ListView listViewHome;
    DrawerLayout drawerLayout;
    FloatingActionButton btGiohang, btSearch;
    ImageView ivbgnav;
    ArrayList<Loaisp> mangloaisp;
    LoaispAdapter loaispAdapter;
    int id = 0;
    String tenloaisp = "";
    String hinhanhloaisp = "";
    ArrayList<Sanpham> mangsanpham;
    SanphamAdapter sanphamAdapter;

    public static ArrayList<com.example.techstore.model.Giohang> manggiohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        tbHome = findViewById(R.id.tbHome);
        viewFlipper = findViewById(R.id.vfHome);
        recyclerViewHome = findViewById(R.id.rvHome);
        navigationViewHome = findViewById(R.id.nvHome);
        listViewHome = findViewById(R.id.lvHome);
        drawerLayout = findViewById(R.id.dlHome);
        btGiohang = findViewById(R.id.btGiohang);
        btSearch = findViewById(R.id.btSearch);
        ivbgnav = findViewById(R.id.ivbgnav);
        Picasso.get().load("https://channel.mediacdn.vn/thumb_w/640/2019/11/9/photo-1-15732726431201850009940.jpg").into(ivbgnav);
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0, new Loaisp(0, "Trang Chủ","https://img.icons8.com/clouds/2x/cottage.png"));
        loaispAdapter = new LoaispAdapter(mangloaisp, getApplicationContext());
        listViewHome.setAdapter(loaispAdapter);
        mangsanpham = new ArrayList<>();
        sanphamAdapter = new SanphamAdapter(getApplicationContext(),mangsanpham);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setAdapter(sanphamAdapter);

        tbHome.setTitle("Trang Chủ");
        Search();
        Giohang();
        if (manggiohang !=null){

        }else {
            manggiohang = new ArrayList<>();
        }
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            ActionBar();
            ActionViewSnipper();
            GetDulieuLoaisp();
            GetDulieuspmoi();
            CatchOnItemListView();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
            finish();
        }
    }

    private void Search() {
        btSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),SearchspActivity.class);
            startActivity(intent);
        });
    }



    public void Giohang(){
        btGiohang.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),Giohang.class);
            startActivity(intent);
        });
    }

    private void CatchOnItemListView() {
        listViewHome.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i){
                case 0:
                    if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case 1:
                    if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                        Intent intent = new Intent(MainActivity.this,DienThoaiActivity.class);
                        intent.putExtra("idloaisanpham",mangloaisp.get(i).getId());
                        startActivity(intent);
                    }
                    else {
                        CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case 2:
                    if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                        Intent intent = new Intent(MainActivity.this,LaptopActivity.class);
                        intent.putExtra("idloaisanpham",mangloaisp.get(i).getId());
                        startActivity(intent);
                    }
                    else {
                        CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case 3:
                    if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                        Intent intent = new Intent(MainActivity.this,LienheActivity.class);
                        startActivity(intent);
                    }
                    else {
                        CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case 4:
                    if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                        Intent intent = new Intent(MainActivity.this,ThongtinActivity.class);
                        startActivity(intent);
                    }
                    else {
                        CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

            }
        });
    }

    private void GetDulieuspmoi() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duongdanspmoi, response -> {
            if (response != null){
                int ID = 0;
                String Tensanpham = "";
                Integer Giasanpham = 0;
                String Hinhanhsanpham = "";
                String Motasanpham = "";
                int IDsanpham = 0;
                for (int i = 0; i< response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ID = jsonObject.getInt("id");
                        Tensanpham = jsonObject.getString("tensp");
                        Giasanpham = jsonObject.getInt("giasp");
                        Hinhanhsanpham = jsonObject.getString("hinhanhsp");
                        Motasanpham = jsonObject.getString("motasp");
                        IDsanpham = jsonObject.getInt("idsanpham");
                        mangsanpham.add(new Sanpham(ID, Tensanpham, Giasanpham, Hinhanhsanpham,Motasanpham, IDsanpham));
                        sanphamAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, error -> {

        });
        requestQueue.add(jsonArrayRequest);
    }

    private void GetDulieuLoaisp() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.DuongdanLoaisp, response -> {
            if (response !=null){
                for (int i = 0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getInt("id");
                        tenloaisp = jsonObject.getString("tenloaisp");
                        hinhanhloaisp = jsonObject.getString("hinhanhloaisp");
                        mangloaisp.add(new Loaisp(id,tenloaisp,hinhanhloaisp));
                        loaispAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mangloaisp.add(3, new Loaisp(0, "Liên hệ","https://img.icons8.com/clouds/2x/apple-mail.png"));
                mangloaisp.add(4, new Loaisp(0, "Thông tin","https://img.icons8.com/clouds/2x/compass.png"));
            }
        }, error -> CheckConnection.ShowToast_Short(getApplicationContext(),error.toString()));
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewSnipper() {
        ArrayList<String> mangquangcao=new ArrayList<>();
        mangquangcao.add("https://cdn.tgdd.vn/2021/02/banner/S21-800-300-800x300.png");
        mangquangcao.add("https://cdn.tgdd.vn/2021/01/banner/sam-oppo-800-300-800x300.png");
        mangquangcao.add("https://cdn.tgdd.vn/2021/02/banner/ED856CE8-0E89-48D7-965C-C1CE7D5BEBF0-800x300.png");
        mangquangcao.add("https://cdn.tgdd.vn/2021/01/banner/800-300-800x300-3.png");
        for (int i=0; i<mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);

    }

    private void ActionBar(){
        tbHome.setNavigationIcon(R.drawable.ic_menu);
        tbHome.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

}