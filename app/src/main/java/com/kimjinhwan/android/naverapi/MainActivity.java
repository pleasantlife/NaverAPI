package com.kimjinhwan.android.naverapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kimjinhwan.android.naverapi.Adapter.ListTypeAdapter;
import com.kimjinhwan.android.naverapi.Util.Items;
import com.kimjinhwan.android.naverapi.Util.LoadDataFromServer;

import java.util.ArrayList;
import java.util.List;


import static com.kimjinhwan.android.naverapi.Util.Client.ITEM_VALUE;
import static com.kimjinhwan.android.naverapi.Util.LoadDataFromServer.lowestPrice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseText, textQueryTime, textLowPrice;
    ImageButton btnSearch;
    EditText query;
    String queryString;
    List<Items> itemList;
    Spinner spinner;
    Switch detailSwitch;
    ProgressDialog dialog;

    LinearLayout linearDetail;
    RecyclerView recyclerView;
    ListTypeAdapter listTypeAdapter;

    long pressedTime = 0;
    long seconds = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //액션바의 그림자를 없앰.
        getSupportActionBar().setElevation(0);

        initView();
        setSpinner();
        itemList = new ArrayList<>();
        listTypeAdapter = new ListTypeAdapter(this);
        recyclerView.setAdapter(listTypeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pressEnterkey();
        switcher();
    }

    public void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textLowPrice = (TextView) findViewById(R.id.textLowPrice);
        textQueryTime = (TextView) findViewById(R.id.textQueryTime);
        query = (EditText) findViewById(R.id.query);
        detailSwitch = (Switch) findViewById(R.id.detailSwitch);
        linearDetail = (LinearLayout) findViewById(R.id.linearDetail);
        linearDetail.setVisibility(View.GONE);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        dialog = new ProgressDialog(this);

    }

    public void setSpinner(){
        final Integer[] viewItem = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, viewItem);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ITEM_VALUE = viewItem[i];
                Log.e("see_result===", ITEM_VALUE +"");
                goSearch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    //검색어 입력 후 엔터 누르면 바로 검색 되도록 함.
    public void pressEnterkey(){
        //setOnKeyListener가 키보드의 입력을 받음.
        query.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == keyEvent.KEYCODE_ENTER){
                    goSearch();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    private void switcher(){
        detailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    linearDetail.setVisibility(View.VISIBLE);
                    detailSwitch.setText("감추기");
                } else {
                    linearDetail.setVisibility(View.GONE);
                    detailSwitch.setText("보기");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSearch:
                goSearch();
                hideKeyboard();
                break;
        }
    }

    public void goSearch(){
        dialog.show();
        lowestPrice = 2147483647;
        queryString = query.getText().toString();
        if(queryString.equals("")){
            Toast.makeText(MainActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            LoadDataFromServer loadData = new LoadDataFromServer(queryString, responseText, textLowPrice, textQueryTime, listTypeAdapter);
            loadData.start();
            listTypeAdapter.notifyDataSetChanged();
        }
        dialog.dismiss();
    }

    //엔터키를 누르고 나면(혹은 상품 검색 버튼을 누르면) InputMethodManage를 통해 키보드를 숨김.
    public void hideKeyboard(){
        InputMethodManager immanager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        immanager.hideSoftInputFromWindow(query.getWindowToken(), 0);
    }

    //back버튼을 3초 내에 두 번 누르면 종료되도록 함.
    @Override
    public void onBackPressed() {

        if(pressedTime == 0){
            Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            seconds = System.currentTimeMillis() - pressedTime;

            if ( seconds > 3000 ) {
                Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.goFavorite:
                Intent favoriteIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(favoriteIntent);
                break;
            case R.id.opensource:
                Intent openSourceIntent = new Intent(MainActivity.this, OpenSourceActivity.class);
                startActivity(openSourceIntent);
        }
        return true;
    }
}


