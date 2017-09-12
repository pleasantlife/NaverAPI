package com.kimjinhwan.android.naverapi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kimjinhwan.android.naverapi.DetailActivity;
import com.kimjinhwan.android.naverapi.R;
import com.kimjinhwan.android.naverapi.Util.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.kimjinhwan.android.naverapi.Util.DBHelper.DATABASE_NAME;
import static com.kimjinhwan.android.naverapi.Util.DBHelper.TABLE_NAME;

/**
 * Created by XPS on 2017-09-11.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Holder> {

    Context context;
    List<FavoriteItem> favoriteItemList = new ArrayList<>();
    SQLiteDatabase database;

    public FavoriteAdapter(Context context) {
        this.context = context;
        setData();
    }

    //SQLite에 저장되어 있는 관심상품 정보를 가져온다.

    public void setData() {
        database = SQLiteDatabase.openOrCreateDatabase("data/data/" + context.getApplicationContext().getPackageName() + "/databases//" + DATABASE_NAME, null);

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        FavoriteItem itemList;
        if (cursor != null && cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                itemList = new FavoriteItem();
                itemList.setTitle(cursor.getString(cursor.getColumnIndex("PRODUCTNAME")));
                itemList.setLink(cursor.getString(cursor.getColumnIndex("LINK")));
                itemList.setImage(cursor.getString(cursor.getColumnIndex("IMAGEURL")));
                itemList.setLprice(cursor.getString(cursor.getColumnIndex("LOWPRICE")));
                itemList.setMallName(cursor.getString(cursor.getColumnIndex("MALLNAME")));

                favoriteItemList.add(itemList);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.e("favoriteItemList", favoriteItemList.toString());
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_listtype, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        FavoriteItem items = new FavoriteItem();
        holder.setPosition(position);
        items = favoriteItemList.get(position);
        long lprice = Long.parseLong(items.getLprice());
        holder.textLPrice.setText(String.format("%,d",lprice)+"원");
        holder.textTitle.setText(items.getTitle());
        holder.textMallName.setText(items.getMallName());
        Glide.with(context).load(items.getImage()).into(holder.imageProducts);

    }

    @Override
    public int getItemCount() {
        return favoriteItemList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView textLPrice, textTitle, textMallName;
        ImageView imageProducts;
        ConstraintLayout eachItem;
        int position;
        public Holder(View itemView) {
            super(itemView);

            textLPrice = itemView.findViewById(R.id.textLPrice);
            imageProducts = itemView.findViewById(R.id.imageProduct);
            textTitle = itemView.findViewById(R.id.textTitle);
            textMallName = itemView.findViewById(R.id.textMallName);
            eachItem = itemView.findViewById(R.id.eachItem);
            eachItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //현재 아이템에 해당하는 Item클래스의 변수와 그 값을 전부 intent로 보냄.
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("position", position);
                    //여기에서 items.get(position)이 아닌 itemList를 보내면 holder의 position 값과 일치하는 데이터를 가져온다!!! 조심!!!
                    //intent.putExtra("itemList", items.get(position));
                    Log.e("position===", position+"");
                    Log.e("itemListForDetail===", favoriteItemList.get(position)+"");
                    context.startActivity(intent);
                }
            });
        }
        public void setPosition(int position){
            this.position = position;
        }
    }

}
