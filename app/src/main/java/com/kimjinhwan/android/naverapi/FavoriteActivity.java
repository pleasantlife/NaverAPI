package com.kimjinhwan.android.naverapi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kimjinhwan.android.naverapi.Adapter.FavoriteAdapter;
import com.kimjinhwan.android.naverapi.Util.DBHelper;
import com.kimjinhwan.android.naverapi.Util.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

import static com.kimjinhwan.android.naverapi.Util.DBHelper.DATABASE_NAME;
import static com.kimjinhwan.android.naverapi.Util.DBHelper.TABLE_NAME;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView recyclerFavorite;
    FavoriteAdapter favoriteAdapter;

    SQLiteDatabase database;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.deleteFavorite:
                break;
            case R.id.deleteFavoriteAll:
                    AlertDialog.OnClickListener positiveListener = new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            database = SQLiteDatabase.openOrCreateDatabase("data/data/" + FavoriteActivity.this.getApplicationContext().getPackageName() + "/databases//" + DATABASE_NAME, null);
                            database.execSQL("DELETE FROM " + TABLE_NAME);
                            favoriteAdapter.notifyDataSetChanged();
                            Toast.makeText(FavoriteActivity.this, "관심 항목이 모두 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    };

                    AlertDialog.OnClickListener negativeListener = new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    };

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this).setMessage("모든 관심 항목을 삭제 하시겠습니까?").setPositiveButton("네", positiveListener).setNegativeButton("아니오", negativeListener);
                    dialog.show();
                break;
        }
        return true;
    }
}
