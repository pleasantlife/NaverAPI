package com.kimjinhwan.android.naverapi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kimjinhwan.android.naverapi.DetailActivity;
import com.kimjinhwan.android.naverapi.R;
import com.kimjinhwan.android.naverapi.Util.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XPS on 2017-09-05.
 */

public class ListTypeAdapter extends RecyclerView.Adapter<ListTypeAdapter.Holder> {

    List<Items> items = new ArrayList<>();
    Items itemList;
    Context context;
    RequestManager requestManager;
    long lprice;

    public ListTypeAdapter(Context context, List<Items> items, RequestManager requestManager){
        this.context = context;
        this.items = items;
        this.requestManager = requestManager;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_listtype, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        itemList = items.get(position);
        holder.setPosition(position);
        holder.textTitle.setText(itemList.getTitle());
        //천단위 마다 ,(콤마)를 찍기 위해 Long으로 변환 후 String format으로 다시 변환함.
        lprice = itemList.getLprice();
        holder.textLPrice.setText(String.format("%,d",lprice)+"원");
        requestManager.load(itemList.getImage()).into(holder.imageProducts);
        holder.textMallName.setText(itemList.getMallName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class Holder extends RecyclerView.ViewHolder{
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
                    intent.putExtra("itemList", items.get(position));
                    Log.e("position===", position+"");
                    Log.e("itemListForDetail===", items.get(position)+"");
                    context.startActivity(intent);
                }
            });
        }
        public void setPosition(int position){
            this.position = position;
        }
    }
}
