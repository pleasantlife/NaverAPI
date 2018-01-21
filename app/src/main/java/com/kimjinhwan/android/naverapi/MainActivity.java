package com.kimjinhwan.android.naverapi;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kimjinhwan.android.naverapi.Adapter.ListTypeAdapter;
import com.kimjinhwan.android.naverapi.Util.CustomLayoutManager;
import com.kimjinhwan.android.naverapi.Util.DBHelper;
import com.kimjinhwan.android.naverapi.Util.Items;
import com.kimjinhwan.android.naverapi.Util.NaverShoppingSearchService;
import com.kimjinhwan.android.naverapi.Util.SearchDataList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kimjinhwan.android.naverapi.Util.DBHelper.DATABASE_NAME;
import static com.kimjinhwan.android.naverapi.Util.DBHelper.DATABASE_VERSION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{


    public static String NAVER_URL = "https://openapi.naver.com/v1/search/";
    TextView responseText, textQueryTime, textLowPrice;
    ImageButton btnSearch;
    EditText query;
    String queryString;
    List<Items> itemList;
    Spinner spinner;
    Switch detailSwitch;
    ProgressBar progressBar;

    LinearLayout linearDetail;
    RecyclerView recyclerView;
    ListTypeAdapter listTypeAdapter;
    CustomLayoutManager customLayoutManager;

    SwipeRefreshLayout swipeRefreshLayout;

    SQLiteDatabase database;

    long pressedTime = 0;
    long seconds = 0;
    int lprice;
    int displayValue = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //액션바의 그림자를 없앰.
        getSupportActionBar().setElevation(0);
        initView();
        setSpinner();
        customLayoutManager = new CustomLayoutManager(this);
        itemList = new ArrayList<>();
        listTypeAdapter = new ListTypeAdapter(this, itemList);
        recyclerView.setAdapter(listTypeAdapter);
        recyclerView.setLayoutManager(customLayoutManager);
        switcher();
        pressEnterkey();
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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        //query.setOnEditorActionListener(this);
        query.setOnKeyListener(new View.OnKeyListener() {
            //OnKey would be called twice, one for a Down event and another one for an Up event. Please try to add a condition:
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP){
                    goSearch();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

    }

    public void pressEnterkey() {
        //setOnKeyListener가 키보드의 입력을 받음.

    }

    public void setSpinner(){
        final Integer[] viewItem = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, viewItem);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                displayValue = viewItem[i];
                goSearch();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    public void setRetrofit(String queryString){
        progressBar.setVisibility(View.VISIBLE);
        textLowPrice.setVisibility(View.INVISIBLE);
        lprice = 2147483647;
        itemList.clear();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NAVER_URL).addConverterFactory(GsonConverterFactory.create()).build();

        NaverShoppingSearchService naverShoppingSearchService = retrofit.create(NaverShoppingSearchService.class);

        Call<SearchDataList> searchDataListCall = naverShoppingSearchService.getSearchList(queryString, displayValue);

        searchDataListCall.enqueue(new Callback<SearchDataList>() {
            @Override
            public void onResponse(Call<SearchDataList> call, Response<SearchDataList> response) {
                if(response.isSuccessful()) {
                    for (Items itemResult : response.body().getItems()) {
                        itemResult.setTitle(itemResult.getTitle().replace("<b>", ""));
                        itemResult.setTitle(itemResult.getTitle().replace("</b>", ""));
                        if (lprice >= itemResult.getLprice()) {
                            lprice = itemResult.getLprice();
                        }
                        itemList.add(itemResult);
                        listTypeAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    if(lprice != 2147483647) {
                        textLowPrice.setText(String.format("%,d", lprice) + "원");
                    } else {
                        textLowPrice.setText("검색 결과 없음");
                        Toast.makeText(MainActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    textLowPrice.setVisibility(View.VISIBLE);
                    Log.e("listItemPositon",
                            customLayoutManager.findLastCompletelyVisibleItemPosition()+"");
                } else {
                    Log.e("error :", "error occured");
                }
            }


            @Override
            public void onFailure(Call<SearchDataList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "에러가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void switcher(){
        detailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    linearDetail.setVisibility(View.VISIBLE);
                    detailSwitch.setText("감추기");
                } else {
                    linearDetail.setVisibility(View.GONE);
                    detailSwitch.setText("보기");
                }
            }
        });
    }

    private boolean networkCheck(){
        boolean connect = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){
            connect = false;
        }
        return connect;
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
        //검색데이터 가져오기!!
        queryString = query.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        if(queryString.equals("")){
            Toast.makeText(MainActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            if(networkCheck()) {
                itemList.clear();
                listTypeAdapter.notifyDataSetChanged();
                setRetrofit(queryString);
                progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "인터넷에 연결되어 있지 않아 검색할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

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
                createDatabase();
                Intent favoriteIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                if(favoriteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(favoriteIntent);
                } else {
                    Toast.makeText(this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.opensource:
                Intent openSourceIntent = new Intent(MainActivity.this, OpenSourceActivity.class);
                if(openSourceIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(openSourceIntent);
                } else {
                    Toast.makeText(this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
        }
        return true;
    }

    public void createDatabase(){
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        DBHelper helper = new DBHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        helper.getWritableDatabase();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setEnabled(false);
        goSearch();
    }



}


