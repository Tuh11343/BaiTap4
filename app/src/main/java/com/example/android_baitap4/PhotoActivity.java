package com.example.android_baitap4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PhotoActivity extends AppCompatActivity {

    private TextView location,title,name;
    private ImageView imgOfficial;
    private View photoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        setUpComponents();
        setUpData();
    }

    public void setUpComponents()
    {
        location=findViewById(R.id.location);
        title=findViewById(R.id.photo_title);
        name=findViewById(R.id.photo_name);
        photoLayout=findViewById(R.id.layout_photo);
        imgOfficial=findViewById(R.id.photo_imgOfficial);
    }

    public void setUpData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        location.setText(bundle.getString(MainActivity.LOCATION_DATA_KEY));
        Official official = (Official) bundle.getSerializable(MainActivity.OFFICIAL_DATA_KEY);
        if (official.getTitle() != null && official.getTitle().length() != 0)
            title.setText(official.getTitle());
        if (official.getName() != null && official.getName().length() != 0)
            name.setText(official.getName());
        photoLayout.setBackgroundColor(getColor(official.getParty().toString()));
        imgOfficial.setImageResource(R.drawable.loadingscreen);
        if (official.getPhotoURL() != null && official.getPhotoURL().length() != 0) {
            imgOfficial.setImageResource(R.drawable.loadingscreen);
            Picasso picasso = new Picasso.Builder(PhotoActivity.this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = official.getPhotoURL().replace("http:", "https:");
                    picasso.load(changedUrl).fit().error(R.drawable.loadingscreen).into(imgOfficial);
                    Log.e("Loi", exception.toString());
                }
            }).build();
            picasso.load(official.getPhotoURL()).fit().error(R.drawable.loadingscreen).into(imgOfficial);
        }
    }

    private int getColor(String party) {
        int color;
        switch (party) {
            case "Republican Party":
                color = Color.RED;
                return color;
            case "Democratic Party":
                color = Color.BLUE;
                return color;
            default:
                color = Color.BLACK;
                return color;
        }
    }
}