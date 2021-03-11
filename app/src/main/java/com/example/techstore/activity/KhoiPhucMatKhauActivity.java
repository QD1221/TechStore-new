package com.example.techstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class KhoiPhucMatKhauActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvdkykp;
    Button bguimail;
    EditText etmailkp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_khoiphuc_matkhau);

        firebaseAuth = FirebaseAuth.getInstance();

        tvdkykp = findViewById(R.id.tvdkykp);
        bguimail = findViewById(R.id.bkpmk);
        etmailkp = findViewById(R.id.etmailkp);

        bguimail.setOnClickListener(this);
        tvdkykp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bkpmk:
                String email = etmailkp.getText().toString();
                boolean ktraemail = KiemTraEmail(email);

                if (ktraemail){
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(KhoiPhucMatKhauActivity.this, getString(R.string.thongbaoguiemailthanhcong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(KhoiPhucMatKhauActivity.this, getString(R.string.thongbaoemailkohople), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvdkykp:
                Intent iDangky = new Intent(KhoiPhucMatKhauActivity.this, DangKyActivity.class);
                startActivity(iDangky);
                break;
        }
    }

    private boolean KiemTraEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
