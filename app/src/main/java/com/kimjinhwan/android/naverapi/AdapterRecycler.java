package com.kimjinhwan.android.naverapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kimjinhwan.android.naverapi.Util.Items;

import java.util.ArrayList;
import java.util.List;

import static com.kimjinhwan.android.naverapi.Util.CustomThread.lowestPrice;

/**
 * Created by XPS on 2017-09-05.
 */

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.Holder> {

    List<Items> items = new ArrayList<>();
    Context context;

    public AdapterRecycler(Context context){
        this.context = context;
    }
    //setData를 통해 파싱한 Json 데이터를 넘겨 받는다. 이걸 하지 않으면 검색하기 전의 Items 클래스만 들어오기 떄문에 아무것도 뜨지 않는다.
    public void setData(List<Items> items){
        this.items = items;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Items itemList = items.get(position);
        holder.setPosition(position);
        holder.textTitle.setText(itemList.getTitle());
        holder.textHPrice.setText(itemList.getHprice());
        holder.textLPrice.setText(itemList.getLprice());
        if(Long.parseLong(itemList.getLprice()) == lowestPrice){
            holder.textLPrice.setTextColor(Color.BLUE);
        }
        Glide.with(context).load(itemList.getImage()).into(holder.imageProducts);
        holder.textMallName.setText(itemList.getMallName());
        holder.textProductid.setText(itemList.getProductId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textProductid, textHPrice, textLPrice, textTitle, textMallName;
        ImageView imageProducts;
        ConstraintLayout eachItem;
        int position;
        public Holder(View itemView) {
            super(itemView);

            textProductid = itemView.findViewById(R.id.textProductId);
            textHPrice = itemView.findViewById(R.id.textHPrice);
            textLPrice = itemView.findViewById(R.id.textLPrice);
            imageProducts = itemView.findViewById(R.id.imageProduct);
            textTitle = itemView.findViewById(R.id.textTitle);
            textMallName = itemView.findViewById(R.id.textMallName);
            eachItem = itemView.findViewById(R.id.eachItem);
            eachItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    Toast.makeText(context, "기다려주세요" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
        public void setPosition(int position){
            this.position = position;
        }
    }
}
