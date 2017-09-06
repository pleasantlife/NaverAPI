package com.kimjinhwan.android.naverapi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kimjinhwan.android.naverapi.Util.Items;

import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    ImageView imageDetail;
    TextView txtTitleDetail, txtLPriceDetail;
    Button btnGoBuy;
    Items itemForDetail = new Items();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        loadDataFromList();
        Log.e("itemSize", itemForDetail.getMallName());
        Log.e("lowPrice===", itemForDetail.getLprice());
        //Log.e("link=====", itemForDetail.getLink());

        setData();

    }

    private void initView(){
        imageDetail = (ImageView) findViewById(R.id.imageDetail);
        txtTitleDetail = (TextView) findViewById(R.id.txtTitleDetail);
        txtLPriceDetail = (TextView) findViewById(R.id.txtLPriceDetail);
        btnGoBuy = (Button) findViewById(R.id.btnGoBuy);
        btnGoBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL uri = null;
                try {
                    uri = new URL(itemForDetail.getLink());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Log.e("uri===", uri+"");
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemForDetail.getLink()));
                //Intent chooser = Intent.createChooser(intent, "웹브라우저 선택");
                //startActivity(chooser);
            }
        });
    }

    //intent를 통해 serialize 된 Items 데이터를 넘겨받음.
    private void loadDataFromList(){
        Intent intent = getIntent();
        itemForDetail = (Items) intent.getSerializableExtra("itemList");
    }

    private void setData(){
        Glide.with(this).load(itemForDetail.getImage()).into(imageDetail);

    }
}
