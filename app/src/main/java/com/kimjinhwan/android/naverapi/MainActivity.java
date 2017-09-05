package com.kimjinhwan.android.naverapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kimjinhwan.android.naverapi.Util.CustomThread;
import com.kimjinhwan.android.naverapi.Util.Items;

import java.util.ArrayList;
import java.util.List;

import static com.kimjinhwan.android.naverapi.Util.CustomThread.lowestPrice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseText, queryTime, textLowPrice;
    Button btnSearch, btnSetGrid, btnSetLinear;
    EditText query;
    String queryString;
    List<Items> itemList;

    RecyclerView recyclerView;
    AdapterRecycler adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        itemList = new ArrayList<>();
        adapter = new AdapterRecycler(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textLowPrice = (TextView) findViewById(R.id.textLowPrice);
        queryTime = (TextView) findViewById(R.id.queryTime);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSetGrid = (Button) findViewById(R.id.btnSetGrid);
        btnSetLinear = (Button) findViewById(R.id.btnSetLinear);
        btnSearch.setOnClickListener(this);
        btnSetGrid.setOnClickListener(this);
        btnSetLinear.setOnClickListener(this);
        query = (EditText) findViewById(R.id.query);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSearch:
                lowestPrice = 2000000000;
                queryString = query.getText().toString();
                if(queryString.equals("")){
                    Toast.makeText(MainActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    CustomThread thread = new CustomThread(queryString, responseText, textLowPrice, adapter);
                    thread.start();
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btnSetGrid:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case R.id.btnSetLinear:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}


