package com.example.techstore.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techstore.R;
import com.example.techstore.controller.DangKyController;
import com.example.techstore.model.ThanhVienModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener {

    Button bdky;
    EditText etmaildk, etmkdk, etmkdk2;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DangKyController dangKyController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        bdky = findViewById(R.id.bdkydk);
        etmaildk = findViewById(R.id.etmaildk);
        etmkdk = findViewById(R.id.etmkdk);
        etmkdk2 = findViewById(R.id.etmkdk2);

        bdky.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        progressDialog.setMessage(getString(R.string.dangxuly));
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        final String email = etmaildk.getText().toString();
        String mk = etmkdk.getText().toString();
        String mk2 = etmkdk2.getText().toString();
        String thongbaoloi = getString(R.string.thongbaoloidk);

        if (email.trim().length() == 0){
            thongbaoloi += getString(R.string.email);
            Toast.makeText(this, thongbaoloi, Toast.LENGTH_SHORT).show();
        }else if (mk.trim().length() == 0 && mk2.trim().length() == 0){
            thongbaoloi += getString(R.string.matkhau);
            Toast.makeText(this, thongbaoloi, Toast.LENGTH_SHORT).show();
        }else if (!mk2.equals(mk)){
            Toast.makeText(this, getString(R.string.thongbaonhaplaimk), Toast.LENGTH_SHORT).show();
        }else {
            firebaseAuth.createUserWithEmailAndPassword(email,mk)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                ThanhVienModel thanhVienModel = new ThanhVienModel();
                                thanhVienModel.setHoten(email);
                                thanhVienModel.setHinhanh("user.png");
                                String uid = task.getResult().getUser().getUid();
                                dangKyController = new DangKyController();
                                dangKyController.ThemThongTinThanhVienController(thanhVienModel, uid);
                                Toast.makeText(DangKyActivity.this, getString(R.string.thongbaodkythanhcong), Toast.LENGTH_SHORT).show();
                                Intent iDangnhap = new Intent(DangKyActivity.this, DangNhapActivity.class);
                                startActivity(iDangnhap);
                            }else {
                                Toast.makeText(DangKyActivity.this, getString(R.string.thongbaodkythatbai), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

