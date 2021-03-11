package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.techstore.R;
import com.example.techstore.adapter.DanhmucAdapter;
import com.example.techstore.adapter.HomeSliderAdapter;
import com.example.techstore.adapter.SanphambanchayAdapter;
import com.example.techstore.adapter.SanphammoiAdapter;
import com.example.techstore.model.DanhmucSanpham;
import com.example.techstore.model.Sanpham;
import com.example.techstore.ultil.CheckConnection;
import com.example.techstore.ultil.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Toolbar tbHome;
    RecyclerView recyclerViewHome, recyclerViewHome2;
    NavigationView navigationViewHome;
    ListView listViewHome;
    DrawerLayout drawerLayout;
    FloatingActionButton btGiohang, btSearch;
    ImageView ivbgnav;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    Timer timer;
    int page_position = 0;
    private int dotscount;
    private ImageView[] dots;
    ArrayList<String> mangquangcao;

    ArrayList<DanhmucSanpham> mangloaisp;
    DanhmucAdapter danhmucAdapter;
    int id = 0;
    String tenloaisp = "";
    String hinhanhloaisp = "";
    ArrayList<Sanpham> mangsanpham, mangsanpham2;
    SanphammoiAdapter sanphammoiAdapter;
    SanphambanchayAdapter sanphambanchayAdapter;

    public static ArrayList<com.example.techstore.model.Giohang> manggiohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tbHome = findViewById(R.id.tbHome);
        recyclerViewHome = findViewById(R.id.rvHome);
        recyclerViewHome2 = findViewById(R.id.rvHome2);
        navigationViewHome = findViewById(R.id.nvHome);
        listViewHome = findViewById(R.id.lvHome);
        drawerLayout = findViewById(R.id.dlHome);
        btGiohang = findViewById(R.id.btGiohang);
        btSearch = findViewById(R.id.btSearch);

        ivbgnav = findViewById(R.id.ivbgnav);
        Picasso.get().load("https://channel.mediacdn.vn/thumb_w/640/2019/11/9/photo-1-15732726431201850009940.jpg").into(ivbgnav);

        mangloaisp = new ArrayList<>();
        mangloaisp.add(0, new DanhmucSanpham(0, "Trang chủ","https://img.icons8.com/ios-glyphs/2x/home.png"));
        danhmucAdapter = new DanhmucAdapter(mangloaisp, getApplicationContext());
        listViewHome.setAdapter(danhmucAdapter);

        mangsanpham = new ArrayList<>();
        sanphammoiAdapter = new SanphammoiAdapter(getApplicationContext(),mangsanpham);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setAdapter(sanphammoiAdapter);

        mangsanpham2 = new ArrayList<>();
        sanphambanchayAdapter = new SanphambanchayAdapter(getApplicationContext(),mangsanpham2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerViewHome2.setHasFixedSize(true);
        recyclerViewHome2.setLayoutManager(gridLayoutManager);
        recyclerViewHome2.setAdapter(sanphambanchayAdapter);

        tbHome.setTitle("Trang chủ");
        Search();
        Giohang();
        if (manggiohang !=null){

        }else {
            manggiohang = new ArrayList<>();
        }
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            ActionBar();
            GetDulieuLoaisp();
            GetDulieuspmoi();
            GetDulieuspbanchay();
            CatchOnItemListView();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
            finish();
        }

        timer = new Timer();
        viewPager = findViewById(R.id.viewPager);
        sliderDotspanel = findViewById(R.id.SliderDots);
        mangquangcao = new ArrayList<>();
        HomeSliderAdapter viewPagerAdapter = new HomeSliderAdapter(this, mangquangcao);
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        scheduleSlider();
    }

    public void scheduleSlider() {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == dotscount) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 400, 4000);
    }

    private void Search() {
        btSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchSanPhamActivity.class);
            startActivity(intent);
        });
    }

    public void Giohang(){
        btGiohang.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GiohangActivity.class);
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

                case 5:
                    if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
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
                int ID;
                String Tensanpham, Hinhanhsanpham, Motasanpham;
                Integer Giasanpham;
                int IDsanpham;
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
                        sanphammoiAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, error -> {

        });
        requestQueue.add(jsonArrayRequest);
    }

    private void GetDulieuspbanchay() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duongdanspbanchay, response -> {
            if (response != null){
                int ID;
                String Tensanpham, Hinhanhsanpham, Motasanpham;
                Integer Giasanpham;
                int IDsanpham;
                for (int i = 0; i< response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ID = jsonObject.getInt("id");
                        Tensanpham = jsonObject.getString("tensp");
                        Giasanpham = jsonObject.getInt("giasp");
                        Hinhanhsanpham = jsonObject.getString("hinhanhsp");
                        Motasanpham = jsonObject.getString("motasp");
                        IDsanpham = jsonObject.getInt("idsanpham");
                        mangsanpham2.add(new Sanpham(ID, Tensanpham, Giasanpham, Hinhanhsanpham,Motasanpham, IDsanpham));
                        sanphambanchayAdapter.notifyDataSetChanged();
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
                        mangloaisp.add(new DanhmucSanpham(id,tenloaisp,hinhanhloaisp));
                        danhmucAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mangloaisp.add(3, new DanhmucSanpham(0, "Liên hệ","https://img.icons8.com/ios-filled/2x/new-contact.png"));
                mangloaisp.add(4, new DanhmucSanpham(0, "Thông tin","https://img.icons8.com/material/2x/about.png"));
                mangloaisp.add(5, new DanhmucSanpham(0, "Đăng xuất", "https://img.icons8.com/metro/2x/exit.png"));
            }
        }, error -> CheckConnection.ShowToast_Short(getApplicationContext(),error.toString()));
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionBar(){
        tbHome.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        tbHome.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

}