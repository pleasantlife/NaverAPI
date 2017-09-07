package com.kimjinhwan.android.naverapi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kimjinhwan.android.naverapi.Adapter.ListTypeAdapter;
import com.kimjinhwan.android.naverapi.Util.Items;
import com.kimjinhwan.android.naverapi.Util.LoadDataFromServer;

import java.util.ArrayList;
import java.util.List;

import static com.kimjinhwan.android.naverapi.Util.Client.SEE_RESULT;
import static com.kimjinhwan.android.naverapi.Util.LoadDataFromServer.lowestPrice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseText, textQueryTime, textLowPrice;
    Button btnSearch;
    ImageView setGrid, setLinear;
    EditText query;
    String queryString;
    List<Items> itemList;
    Spinner spinner;

    RecyclerView recyclerView;
    ListTypeAdapter listTypeAdapter;


    public static int SET_VIEW_TYPE = 1111;
    public static int SET_VIEW_LINEAR = 1111;
    public static int SET_VIEW_GRID = 2222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Integer[] viewItem = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

        initView();
        itemList = new ArrayList<>();
        listTypeAdapter = new ListTypeAdapter(this);
        recyclerView.setAdapter(listTypeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pressEnterkey();

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, viewItem);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SEE_RESULT = viewItem[i];
                Log.e("see_result===", SEE_RESULT+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textLowPrice = (TextView) findViewById(R.id.textLowPrice);
        textQueryTime = (TextView) findViewById(R.id.textQueryTime);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        setGrid = (ImageView) findViewById(R.id.setGrid);
        setLinear = (ImageView) findViewById(R.id.setLinear);
        btnSearch.setOnClickListener(this);
        setGrid.setOnClickListener(this);
        setLinear.setOnClickListener(this);
        query = (EditText) findViewById(R.id.query);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSearch:
                goSearch();
                hideKeyboard();
                break;
            case R.id.setGrid:
                SET_VIEW_TYPE = SET_VIEW_GRID;
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case R.id.setLinear:
                SET_VIEW_TYPE = SET_VIEW_LINEAR;
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
    }

    public void goSearch(){
        lowestPrice = 2000000000;
        queryString = query.getText().toString();
        if(queryString.equals("")){
            Toast.makeText(MainActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            LoadDataFromServer loadData = new LoadDataFromServer(queryString, responseText, textLowPrice, textQueryTime, listTypeAdapter);
            loadData.start();
            listTypeAdapter.notifyDataSetChanged();
        }
    }

    //엔터키를 누르고 나면(혹은 상품 검색 버튼을 누르면) InputMethodManage를 통해 키보드를 숨김.
    public void hideKeyboard(){
        InputMethodManager immanager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        immanager.hideSoftInputFromWindow(query.getWindowToken(), 0);
    }
}


