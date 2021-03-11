package com.example.techstore.controller;

import com.example.techstore.model.ThanhVienModel;

public class DangKyController {
    ThanhVienModel thanhVienModel;

    public DangKyController(){
        thanhVienModel = new ThanhVienModel();
    }

    public void ThemThongTinThanhVienController(ThanhVienModel thanhVienModel, String uid){
        thanhVienModel.ThemThongTinThanhVien(thanhVienModel, uid);
    }
}
