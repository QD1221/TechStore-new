package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.techstore.R;
import com.example.techstore.adapter.DanhmucAdapter;
import com.example.techstore.adapter.GiohangAdapter;
import com.example.techstore.adapter.HomeSliderAdapter;
import com.example.techstore.adapter.SanphambanchayAdapter;
import com.example.techstore.adapter.SanphammoiAdapter;
import com.example.techstore.model.DanhmucSanpham;
import com.example.techstore.model.Giohang;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String NIGHT_MODE = "NIGHT_MODE";
    private static final String PREF = "AppSettingsPrefs";
    private static final String FIRST_START = "FirstStart";

    FirebaseAuth firebaseAuth;
    Toolbar tbHome;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerViewHome, recyclerViewHome2;
    NavigationView navigationViewHome;
    ListView listViewHome;
    DrawerLayout drawerLayout;

    ImageView ivsearch;
    FrameLayout flcart;
    TextView tvcart;

    TextView tvtenuser;
    CircleImageView civuser;

    ImageView ivout;

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

    public static ArrayList<Giohang> manggiohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences appSettingsPrefs = MainActivity.this.getSharedPreferences(PREF,0);
        final boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, true);
        boolean isFirstStart = appSettingsPrefs.getBoolean(FIRST_START, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isFirstStart ){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }

        tbHome = findViewById(R.id.tbHome);
        recyclerViewHome = findViewById(R.id.rvHome);
        recyclerViewHome2 = findViewById(R.id.rvHome2);
        navigationViewHome = findViewById(R.id.nvHome);
        listViewHome = findViewById(R.id.lvHome);
        drawerLayout = findViewById(R.id.dlHome);

        ivsearch = findViewById(R.id.ivsearch);
        flcart = findViewById(R.id.flcart);
        tvcart = findViewById(R.id.tvcart);


        tvtenuser = findViewById(R.id.tvtenuser);
        civuser = findViewById(R.id.civuser);

        ivout = findViewById(R.id.ivout);
        ivout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();

            }
        });

        sharedPreferences = getSharedPreferences("luudangnhap", MODE_PRIVATE);

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

        if (manggiohang !=null){

        }else {
            manggiohang = new ArrayList<>();
        }

        tbHome.setTitle("Trang chủ");
        Search();
        Giohang();

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
        ivsearch.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchSanPhamActivity.class);
            startActivity(intent);
        });
    }

    public void Giohang(){
        if (manggiohang.size() != 0){
            tvcart.setText(String.valueOf(manggiohang.size()));
        }else {
            tvcart.setText("0");
        }
        flcart.setOnClickListener(v -> {
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
                        Intent intent = new Intent(MainActivity.this, CaiDatActivity.class);
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
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Herokuspmoi, response -> {
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
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Herokuspbanchay, response -> {

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
        String tenuser = sharedPreferences.getString("tenuser", "");
        if (!tenuser.equals("")||tenuser.length()!= 0){
            tvtenuser.setText(tenuser);
        }else {
            tvtenuser.setText("Thành viên");
        }

        String hinhuser = sharedPreferences.getString("hinhuser", "");
        Picasso.get().load(hinhuser).error(getDrawable(R.drawable.user)).into(civuser);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.DuongdanLoaisp, response -> {
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Herokuloaisp, response -> {

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
                mangloaisp.add(5, new DanhmucSanpham(0, "Giao diện", "https://img.icons8.com/fluent-systems-filled/2x/crescent-moon.png"));
            }
        }, error -> CheckConnection.ShowToast_Short(getApplicationContext(),error.toString()));
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionBar(){
        tbHome.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        tbHome.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

    private void showCustomDialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(MainActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_dangxuat);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvdongy = dialog.findViewById(R.id.tvdongy);
        TextView tvkodongy = dialog.findViewById(R.id.tvkodongy);
        tvdongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
                startActivity(intent);
            }
        });

        tvkodongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Set dialog title

        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Giohang();
    }
}