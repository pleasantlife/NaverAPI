package com.kimjinhwan.android.naverapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kimjinhwan.android.naverapi.Adapter.FavoriteAdapter;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView recyclerFavorite;
    FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initView();
        favoriteAdapter = new FavoriteAdapter(this);
        recyclerFavorite.setAdapter(favoriteAdapter);
        recyclerFavorite.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initView(){
        recyclerFavorite = (RecyclerView) findViewById(R.id.recyclerFavorite);
    }
}
