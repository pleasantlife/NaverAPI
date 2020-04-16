package com.kimjinhwan.android.naverapi;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kimjinhwan.android.naverapi.Adapter.FavoriteAdapter;
import com.kimjinhwan.android.naverapi.Util.DBHelper;
import com.kimjinhwan.android.naverapi.Util.FavoriteItem;

import static com.kimjinhwan.android.naverapi.Util.DBHelper.DATABASE_NAME;
import static com.kimjinhwan.android.naverapi.Util.DBHelper.TABLE_NAME;

public class DetailFavoriteActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageDetailFavorite;
    Button btnGoBuyFavorite, btnDelFavorite;
    TextView txtTitleFavorite, txtLPriceFavorite;
    FavoriteItem items;
    FavoriteAdapter adapter;

    SQLiteDatabase database;
    DBHelper helper;
    int id;
    long lowPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_detail_favorite);
        adapter = new FavoriteAdapter(this);
        initView();
        loadData();
    }

    private void initView(){
        imageDetailFavorite = (ImageView) findViewById(R.id.imageDetailFavorite);
        btnGoBuyFavorite = (Button) findViewById(R.id.btnGoBuyFavorite);
        txtTitleFavorite = (TextView) findViewById(R.id.txtTitleDetail);
        txtLPriceFavorite = (TextView) findViewById(R.id.txtLPriceFavorite);
        btnGoBuyFavorite.setOnClickListener(this);
        btnDelFavorite = (Button) findViewById(R.id.btnDelFavorite);
        btnDelFavorite.setOnClickListener(this);
    }

    private void loadData(){
        Intent intent = getIntent();
        items = (FavoriteItem) intent.getSerializableExtra("itemList");
        setData(items);
        id = intent.getIntExtra("position", 0);
        Log.e("id===", id+"");

    }

    private void setData(FavoriteItem items){
        Glide.with(this).load(items.getImage()).into(imageDetailFavorite);
        txtTitleFavorite.setText(items.getTitle());
        lowPrice = Long.parseLong(items.getLprice());
        txtLPriceFavorite.setText(String.format("%,d", lowPrice)+"원");


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnGoBuyFavorite:
                Uri uri = Uri.parse(items.getLink());
                Intent intentGoBuy = new Intent();
                intentGoBuy.setAction(Intent.ACTION_VIEW);
                intentGoBuy.setData(uri);
                startActivity(intentGoBuy);
                break;
            case R.id.btnDelFavorite:
                database = SQLiteDatabase.openOrCreateDatabase("data/data/" + this.getApplicationContext().getPackageName() + "/databases//" + DATABASE_NAME, null);
                database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE PRODUCTNAME=" + "'" + items.getTitle() + "';");
                Intent intentDelFavorite = new Intent(this, FavoriteActivity.class);
                intentDelFavorite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentDelFavorite);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "관심 품목을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
