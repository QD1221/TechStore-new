package com.example.techstore.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techstore.R;
import com.example.techstore.controller.DangKyController;
import com.example.techstore.model.ThanhVienModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

public class DangNhapActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener {

    Button bdngg, bdnfb, bdn;
    TextView tvdky, tvquenmk;
    EditText etmaildn, etmkdn;

    GoogleSignInClient signInClient;

    public static int REQUEST_CODE_DANGNHAP_GG = 32;
    public static int KIEMTRA_PROVIDER_DANGNHAP = 0;

    FirebaseAuth firebaseAuth;
    CallbackManager callbackFacebook;
    LoginManager loginFacebook;
    List<String> permissionFacebook = Arrays.asList("email","public_profile");

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangnhap);

        callbackFacebook = CallbackManager.Factory.create();
        loginFacebook = LoginManager.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

//        firebaseAuth.signOut();

        bdngg = findViewById(R.id.bdngg);
        bdnfb = findViewById(R.id.bdnfb);
        tvdky = findViewById(R.id.tvdky);
        tvquenmk = findViewById(R.id.tvquenmk);
        bdn = findViewById(R.id.bdangnhap);
        etmaildn = findViewById(R.id.etmaildn);
        etmkdn = findViewById(R.id.etmkdn);

        bdngg.setOnClickListener(this);
        bdnfb.setOnClickListener(this);
        tvdky.setOnClickListener(this);
        tvquenmk.setOnClickListener(this);
        bdn.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("luudangnhap", MODE_PRIVATE);

        KhoiTaoDangNhapGoogle();
    }


    private void DangNhapFacebook() {
        loginFacebook.logInWithReadPermissions(DangNhapActivity.this, permissionFacebook);
        loginFacebook.registerCallback(callbackFacebook, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                KIEMTRA_PROVIDER_DANGNHAP = 2;
                String tokenID = loginResult.getAccessToken().getToken();
                ChungThucDangNhapFirebase(tokenID);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

        });
    }

    private void KhoiTaoDangNhapGoogle(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("488310439256-vjdgooasgfavpm79m49vme3617r71osv.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    private void DangNhapGoogle(){
        KIEMTRA_PROVIDER_DANGNHAP = 1;
        Intent iDnGG = signInClient.getSignInIntent();
        startActivityForResult(iDnGG, REQUEST_CODE_DANGNHAP_GG);
    }

    private void ChungThucDangNhapFirebase(String tokenID){
        if (KIEMTRA_PROVIDER_DANGNHAP == 1){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenID, null);
            firebaseAuth.signInWithCredential(authCredential);
        }else if (KIEMTRA_PROVIDER_DANGNHAP == 2){
            AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
            firebaseAuth.signInWithCredential(authCredential);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DANGNHAP_GG){
            if (resultCode == RESULT_OK) {
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount account = signInResult.getSignInAccount();
                String personName = account.getDisplayName();
                String tokenID = account.getIdToken();
                ChungThucDangNhapFirebase(tokenID);


//
            }
        }else {
            callbackFacebook.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bdngg:
                DangNhapGoogle();
                break;

            case R.id.bdnfb:
                DangNhapFacebook();
                break;

            case R.id.tvdky:
                Intent iDangky = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(iDangky);
                break;

            case R.id.bdangnhap:
                DangNhap();
                break;

            case R.id.tvquenmk:
                Intent iKhoiphucmk = new Intent(DangNhapActivity.this, KhoiPhucMatKhauActivity.class);
                startActivity(iKhoiphucmk);
                break;
        }
    }

    private void DangNhap(){
        final String email = etmaildn.getText().toString();
        final String password = etmkdn.getText().toString();
        if ((email.equals("")||email.length() == 0) || (password.equals("")||password.length() == 0)){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(DangNhapActivity.this, getString(R.string.thongbaodnthatbai), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){

            ThanhVienModel thanhVienModel = new ThanhVienModel();

            String tenthanhvien = firebaseAuth.getCurrentUser().getDisplayName();
            if (tenthanhvien != null){
                thanhVienModel.setHoten(tenthanhvien);
            }else {
                thanhVienModel.setHoten("Thành viên");
            }


            Uri uri = firebaseAuth.getCurrentUser().getPhotoUrl();
            if (uri != null){
                thanhVienModel.setHinhanh(uri.toString());
            }else {
                thanhVienModel.setHinhanh("user.png");
            }

            DangKyController dangKyController = new DangKyController();
            dangKyController.ThemThongTinThanhVienController(thanhVienModel, user.getUid());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("mauser", user.getUid());
            editor.commit();

            Intent iTrangchu = new Intent(DangNhapActivity.this, MainActivity.class);
            startActivity(iTrangchu);
        }
    }

}
