package com.example.techstore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.techstore.R;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LienheActivity extends AppCompatActivity {

    Toolbar tbLienhe;
    TextView tvgit, tvfb, tvmail;

    private static final String GITHUB_LINK = "https://github.com/QD1221";
    private static final String FACEBOOK_PROFILE = "https://www.facebook.com/profile.php?id=100010952103383";
    private static final String GMAIL = "mailto:qd12122000@gmail.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lienhe);
        tbLienhe = findViewById(R.id.tbLienhe);
        tvgit = findViewById(R.id.tvgit);
        tvfb = findViewById(R.id.tvfb);
        tvmail = findViewById(R.id.tvmail);
        ActionToolbar();
        LinkBuilder.on(tvgit).addLinks(getExampleLinks()).build();
        LinkBuilder.on(tvfb).addLinks(getExampleLinks()).build();
        LinkBuilder.on(tvmail).addLinks(getExampleLinks()).build();
    }

    private void ActionToolbar(){
        tbLienhe.setNavigationIcon(R.drawable.ic_back);
        tbLienhe.setNavigationOnClickListener(v -> finish());
    }

    private List<Link> getExampleLinks() {
        List<Link> links = new ArrayList<>();

        // create a single click link to the github page
        Link github = new Link("QD1221");
        github.setTextColor(Color.parseColor("#131313"));
        github.setTypeface(Typeface.DEFAULT_BOLD)
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        openLink(GITHUB_LINK);
                    }
                });

        // create a single click link to the matched twitter profiles
        Link mentions = new Link("Quang");
        mentions.setTextColor(Color.parseColor("#00BCD4"));
        mentions.setHighlightAlpha(.4f);
        mentions.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                openLink(FACEBOOK_PROFILE);
            }
        });

        // match the numbers that I created
        Link numbers = new Link(Pattern.compile("[0-9]+"));
        numbers.setTextColor(Color.parseColor("#FF9800"));
        numbers.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                showToast("Clicked: " + clickedText);
            }
        });


        // link to our play store page
        Link playStore = new Link("qd12122000@gmail.com");
        playStore.setTextColor(Color.parseColor("#FF9800"));
        playStore.setTextColorOfHighlightedLink(Color.parseColor("#10be58"));
        playStore.setHighlightAlpha(0f);
        playStore.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                openLink(GMAIL);
            }
        });

        links.add(github);
        links.add(mentions);
        links.add(numbers);
        links.add(playStore);

        return links;
    }

    private void openLink(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}