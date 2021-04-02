package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.techstore.R;
import com.example.techstore.model.ChiTietDonHangModel;
import com.example.techstore.model.DonHangModel;
import com.example.techstore.model.Sanpham;
import com.example.techstore.ultil.CheckConnection;
import com.example.techstore.ultil.Server;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ThongtinKHActivity extends AppCompatActivity {

    EditText etTenKH,etSDT,etEmail;
    Button btXacnhan;
    ImageView btTrove;

    String madonhang;

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
        btXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThongtinKHActivity.this);

                builder.setTitle("Xác nhận gửi thông tin");
                builder.setIcon(ContextCompat.getDrawable(ThongtinKHActivity.this, R.drawable.warning2));
                builder.setMessage("Bạn đã chắc chắn điền đúng thông tin chưa? Nếu đúng, vui lòng ấn Tiếp tục");
                builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String ten = etTenKH.getText().toString().trim();
                        final String sdt = etSDT.getText().toString().trim();
                        final String email = etEmail.getText().toString().trim();
                        if (ten.length() > 0 && sdt.length() > 0 && email.length() > 0) {

                            DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();

                            DatabaseReference nodeDonhang = nodeRoot.child("donhang");
                            madonhang = nodeDonhang.push().getKey();

                            DonHangModel donHangModel = new DonHangModel();
                            donHangModel.setTenkhachhang(ten);
                            donHangModel.setSodienthoai(sdt);
                            donHangModel.setEmail(email);

                            for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                                ChiTietDonHangModel chiTietDonHangModel = new ChiTietDonHangModel();

                                chiTietDonHangModel.setTensanpham(MainActivity.manggiohang.get(i).getTensp());
                                chiTietDonHangModel.setGiasanpham(MainActivity.manggiohang.get(i).getGiasp());
                                chiTietDonHangModel.setSoluongsanpham(MainActivity.manggiohang.get(i).getSoluongsp());

                                nodeRoot.child("chitietdonhang").child(madonhang).push().setValue(chiTietDonHangModel);

                            }

                            nodeDonhang.child(madonhang).setValue(donHangModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ThongtinKHActivity.this, "Them thanh cong", Toast.LENGTH_SHORT).show();
                                }
                            });









                            // PHP
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdandonhang, new Response.Listener<String>() {
                                @Override
                                public void onResponse(final String madonhang) {
                                    if (madonhang != null || parseInt(madonhang) > 0) {
                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                        StringRequest request = new StringRequest(Request.Method.POST, Server.Duongdanchitietdonhang, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response != null) {
                                                    MainActivity.manggiohang.clear();
                                                    showCustomDialog();
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
                            CheckConnection.ShowToast_Short(getApplicationContext(), "Vui lòng kiểm tra lại thông tin");
                        }
                    }
                });

                builder.setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void showCustomDialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(ThongtinKHActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_xacnhanthongtin);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvdialog = dialog.findViewById(R.id.tvdialog);
        tvdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThongtinKHActivity.this, MainActivity.class));
                finish();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startActivity(new Intent(ThongtinKHActivity.this, MainActivity.class));
                finish();
            }
        });
        // Set dialog title

        dialog.show();

    }
}