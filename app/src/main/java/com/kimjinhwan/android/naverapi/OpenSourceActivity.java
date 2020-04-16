package com.kimjinhwan.android.naverapi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OpenSourceActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageNaverAPI;
    TextView linkGlide, linkGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);
        initView();
    }

    private void initView(){
        imageNaverAPI = (ImageView) findViewById(R.id.imageNaverAPI);
        imageNaverAPI.setOnClickListener(this);
        linkGlide = (TextView) findViewById(R.id.linkGlide);
        linkGlide.setOnClickListener(this);
        linkGson = (TextView) findViewById(R.id.linkGson);
        linkGson.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imageNaverAPI:
                Uri uriNaver = Uri.parse("https://developers.naver.com");
                Intent intentNaver = new Intent();
                intentNaver.setAction(Intent.ACTION_VIEW);
                intentNaver.setData(uriNaver);
                startActivity(intentNaver);
                break;
            case R.id.linkGlide:
                Uri uriGlide = Uri.parse("https://github.com/bumptech/glide");
                Intent intentGlide = new Intent();
                intentGlide.setAction(Intent.ACTION_VIEW);
                intentGlide.setData(uriGlide);
                startActivity(intentGlide);
                break;
            case R.id.linkGson:
                Uri uriGson = Uri.parse("https://github.com/google/gson");
                Intent intentGson = new Intent();
                intentGson.setAction(Intent.ACTION_VIEW);
                intentGson.setData(uriGson);
                startActivity(intentGson);
                break;
        }
    }
}
