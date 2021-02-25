package com.example.techstore.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.techstore.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SearchspActivity extends AppCompatActivity {
    Toolbar tbsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchsp);

        tbsearch = findViewById(R.id.tbsearch);
        tbsearch.setNavigationIcon(R.drawable.ic_back);
        tbsearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}