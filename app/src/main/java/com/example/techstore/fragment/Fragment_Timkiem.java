package com.example.techstore.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techstore.R;
import com.example.techstore.adapter.SearchSanphamAdapter;
import com.example.techstore.model.Sanpham;
import com.example.techstore.ultil.APIService;
import com.example.techstore.ultil.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;


public class Fragment_Timkiem extends Fragment {
    View view;
    Toolbar tbsearch;
    RecyclerView rvsearch;
    TextView tvkocodulieu, tvsearch;
    SearchSanphamAdapter searchSanphamAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timkiem, container, false);
        tbsearch = view.findViewById(R.id.tbsearch);
        rvsearch = view.findViewById(R.id.rvsearch);
        tvkocodulieu = view.findViewById(R.id.tvkocodulieu);
        tvsearch = view.findViewById(R.id.tvsearch);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tbsearch);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        tbsearch.setTitle("Tìm kiếm");
        setHasOptionsMenu(true);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setQueryHint("Nhập từ khóa");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchTuKhoaSanPham(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void SearchTuKhoaSanPham (String query) {
        DataService dataService = APIService.getService();
        Call<List<Sanpham>> callback = dataService.getSearchSanpham(query);
        callback.enqueue(new Callback<List<Sanpham>>() {
            @Override
            public void onResponse(Call<List<Sanpham>> call, Response<List<Sanpham>> response) {
                ArrayList<Sanpham> mangsanpham = (ArrayList<Sanpham>) response.body();
                if (mangsanpham.size() >0){
                    searchSanphamAdapter = new SearchSanphamAdapter(getActivity(), mangsanpham);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    tvsearch.setVisibility(View.VISIBLE);
                    tvsearch.setText("Có " + String.valueOf(mangsanpham.size()) + " sản phẩm được tìm thấy");
                    rvsearch.setLayoutManager(linearLayoutManager);
                    rvsearch.setAdapter(searchSanphamAdapter);
                    tvkocodulieu.setVisibility(GONE);
                    rvsearch.setVisibility(View.VISIBLE);
                }else {
                    rvsearch.setVisibility(GONE);
                    tvkocodulieu.setVisibility(View.VISIBLE);
                    tvsearch.setVisibility(GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Sanpham>> call, Throwable t) {

            }
        });
    }

}
