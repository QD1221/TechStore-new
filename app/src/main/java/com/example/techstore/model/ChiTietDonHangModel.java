package com.example.techstore.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChiTietDonHangModel implements Parcelable {
    String tensanpham;
    double giasanpham, soluongsanpham;

    public ChiTietDonHangModel(){

    }

    protected ChiTietDonHangModel(Parcel in) {
        tensanpham = in.readString();
        giasanpham = in.readDouble();
        soluongsanpham = in.readDouble();
    }

    public static final Creator<ChiTietDonHangModel> CREATOR = new Creator<ChiTietDonHangModel>() {
        @Override
        public ChiTietDonHangModel createFromParcel(Parcel in) {
            return new ChiTietDonHangModel(in);
        }

        @Override
        public ChiTietDonHangModel[] newArray(int size) {
            return new ChiTietDonHangModel[size];
        }
    };

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public double getGiasanpham() {
        return giasanpham;
    }

    public void setGiasanpham(double giasanpham) {
        this.giasanpham = giasanpham;
    }

    public double getSoluongsanpham() {
        return soluongsanpham;
    }

    public void setSoluongsanpham(double soluongsanpham) {
        this.soluongsanpham = soluongsanpham;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tensanpham);
        dest.writeDouble(giasanpham);
        dest.writeDouble(soluongsanpham);
    }
}
