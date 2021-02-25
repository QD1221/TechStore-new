package com.example.techstore.ultil;


import com.example.techstore.model.Sanpham;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DataService {

    @FormUrlEncoded
    @POST("timkiemsanpham.php")
    Call<List<Sanpham>> getSearchSanpham(@Field("tukhoa") String tukhoa);

}

