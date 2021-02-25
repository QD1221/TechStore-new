package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.techstore.R;
import com.example.techstore.ultil.CheckConnection;
import com.example.techstore.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ThongtinKH extends AppCompatActivity {

    EditText etTenKH,etSDT,etEmail;
    Button btXacnhan,btTrove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin_k_h);
        etTenKH = findViewById(R.id.etTenKH);
        etSDT = findViewById(R.id.etSDTKH);
        etEmail = findViewById(R.id.etEmailKH);
        btXacnhan = findViewById(R.id.btXacnhan);
        btTrove = findViewById(R.id.btTrove);
        btTrove.setOnClickListener(v -> finish());
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            EventButton();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
        }
    }

    private void EventButton() {
        btXacnhan.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        final String ten = etTenKH.getText().toString().trim();
        final String sdt = etSDT.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        if (ten.length() > 0 && sdt.length() > 0 && email.length() > 0) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdandonhang, new Response.Listener<String>() {
                @Override
                public void onResponse(final String madonhang) {
                    Log.d("Ma don hang", madonhang);
                    if (madonhang != null || parseInt(madonhang) > 0) {
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest request = new StringRequest(Request.Method.POST, Server.Duongdanchitietdonhang, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null) {
                                    MainActivity.manggiohang.clear();
                                    CheckConnection.ShowToast_Short(getApplicationContext(), "Đã thêm dữ liệu giỏ hàng thành công");
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    CheckConnection.ShowToast_Short(getApplicationContext(), "Mời tiếp tục mua hàng");
                                } else {
                                    CheckConnection.ShowToast_Short(getApplicationContext(), "Dữ liệu giỏ hàng bị lỗi");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }

                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                JSONArray jsonArray = new JSONArray();
                                for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("madonhang", madonhang);
                                        jsonObject.put("masanpham", MainActivity.manggiohang.get(i).getIdsp());
                                        jsonObject.put("tensanpham", MainActivity.manggiohang.get(i).getTensp());
                                        jsonObject.put("giasanpham", MainActivity.manggiohang.get(i).getGiasp());
                                        jsonObject.put("soluongsanpham", MainActivity.manggiohang.get(i).getSoluongsp());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    jsonArray.put(jsonObject);
                                }
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("json", jsonArray.toString());
                                return hashMap;
                            }
                        };
                        queue.add(request);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("tenkhachhang", ten);
                    hashMap.put("sodienthoai", sdt);
                    hashMap.put("email", email);
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest);
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Hãy kiểm tra lại dữ liệu");
        }
    }
}