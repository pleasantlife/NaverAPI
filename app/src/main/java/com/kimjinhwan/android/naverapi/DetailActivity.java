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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageDetail;
    TextView txtTitleDetail, txtLPriceDetail;
    Button btnGoBuy;
    Items itemList;
    Uri uri = null;
    long lowPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        loadDataFromList();
        Log.e("mallName===", itemList.getMallName());
        Log.e("lowPrice===", itemList.getLprice());
        //Log.e("link=====", itemForDetail.getLink());

        setData();

    }

    //intent를 통해 serialize 된 Items 데이터를 넘겨받음.
    private void loadDataFromList() {
        Intent intent = getIntent();
        itemList = (Items) intent.getSerializableExtra("itemList");
        lowPrice = Long.parseLong(itemList.getLprice());
        uri = Uri.parse(itemList.getLink());
    }

    private void initView() {
        imageDetail = (ImageView) findViewById(R.id.imageDetail);
        txtTitleDetail = (TextView) findViewById(R.id.txtTitleDetail);
        txtLPriceDetail = (TextView) findViewById(R.id.txtLPriceDetail);
        btnGoBuy = (Button) findViewById(R.id.btnGoBuy);
        btnGoBuy.setOnClickListener(this);

    }


    private void setData() {
        Glide.with(this).load(itemList.getImage()).into(imageDetail);
        txtTitleDetail.setText(itemList.getTitle());
        txtLPriceDetail.setText(String.format("%,d",lowPrice)+"원");

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnGoBuy:
                Intent linkIntent = new Intent();
                linkIntent.setAction(Intent.ACTION_VIEW);
                linkIntent.setData(uri);
                startActivity(linkIntent);
                break;
        }

    }

}
