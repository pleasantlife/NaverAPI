package com.kimjinhwan.android.naverapi;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kimjinhwan.android.naverapi.Adapter.ListTypeAdapter;
import com.kimjinhwan.android.naverapi.Util.CustomLayoutManager;
import com.kimjinhwan.android.naverapi.Util.DBHelper;
import com.kimjinhwan.android.naverapi.Util.Items;
import com.kimjinhwan.android.naverapi.Util.NaverShoppingSearchService;
import com.kimjinhwan.android.naverapi.Util.SearchDataList;

import java.util.ArrayList;
import java.util.Collections;
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

    TextView textLowPrice;
    ImageButton btnSearch;
    EditText query;
    String queryString;
    List<Items> itemList;
    Switch detailSwitch;
    ProgressBar progressBar;
    RadioButton radioBtnSim, radioBtnPrice;
    RadioGroup radioGroupSort;

    LinearLayout linearDetail;
    RecyclerView recyclerView;
    ListTypeAdapter listTypeAdapter;
    CustomLayoutManager customLayoutManager;

    SwipeRefreshLayout swipeRefreshLayout;

    SQLiteDatabase database;

    RequestManager requestManager;

    long pressedTime = 0;
    long seconds = 0;
    int lprice;
    int displayValue = 100;
    int startValue = 1;
    String sortType = "sim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestManager = Glide.with(this);


        //액션바의 그림자를 없앰.
        getSupportActionBar().setElevation(0);
        initView();
        //setSpinner();
        customLayoutManager = new CustomLayoutManager(this);
        itemList = new ArrayList<>();
        listTypeAdapter = new ListTypeAdapter(this, itemList, requestManager);
        recyclerView.setAdapter(listTypeAdapter);
        recyclerView.setLayoutManager(customLayoutManager);
        //switcher();
        pressEnterkey();
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("count", recyclerView.getLayoutManager().getItemCount()+"");
                Log.e("lastVisible", customLayoutManager.findLastVisibleItemPosition()+"");
                int lastVisible = customLayoutManager.findLastVisibleItemPosition();
                /*if(lastVisible +1 == itemList.size()){
                    startValue = startValue + 10;
                    setRetrofit(queryString);
                }*/
                if(lastVisible == itemList.size()-1){
                    startValue = startValue + 100;
                    setRetrofit(queryString);
                }
            }
        });
    }

    public void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textLowPrice = (TextView) findViewById(R.id.textLowPrice);
        query = (EditText) findViewById(R.id.query);

        radioBtnPrice = (RadioButton) findViewById(R.id.radioBtnPrice);
        radioBtnSim = (RadioButton) findViewById(R.id.radioBtnSim);
        radioBtnSim.setChecked(true);
        radioGroupSort = (RadioGroup) findViewById(R.id.radioGroupSort);
        radioGroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioBtnSim:
                        if(!query.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "검색어와 유사한 물품을 검색합니다.", Toast.LENGTH_SHORT).show();
                            sortType = "sim";
                            clearData();
                            setRetrofit(queryString);
                        }
                        break;
                    case R.id.radioBtnPrice:
                        if(!query.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "최저가 순으로 검색합니다.", Toast.LENGTH_SHORT).show();
                            sortType = "asc";
                            clearData();
                            setRetrofit(queryString);
                        }
                        break;
                }
            }
        });

        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!query.getText().toString().isEmpty()){
                    swipeRefreshLayout.setRefreshing(false);
                    clearData();
                    Toast.makeText(MainActivity.this, "다시 검색합니다.", Toast.LENGTH_SHORT).show();
                    setRetrofit(queryString);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void clearData(){
        itemList.clear();
        startValue = 1;
        lprice = 2147483647;
    }


    public void setRetrofit(String queryString){
        progressBar.setVisibility(View.VISIBLE);
        textLowPrice.setVisibility(View.INVISIBLE);

        if(startValue == 1) {
            itemList.clear();
            lprice = 2147483647;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(NAVER_URL).addConverterFactory(GsonConverterFactory.create()).build();

        NaverShoppingSearchService naverShoppingSearchService = retrofit.create(NaverShoppingSearchService.class);

        Call<SearchDataList> searchDataListCall = naverShoppingSearchService.getSearchList(queryString, displayValue, startValue, sortType);

        searchDataListCall.enqueue(new Callback<SearchDataList>() {
            @Override
            public void onResponse(Call<SearchDataList> call, Response<SearchDataList> response) {
                if(response.isSuccessful()) {
                    if (response.body().getDisplay() != 0) {
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
                        if (itemList.size() == 0) {
                            lprice = 2147483647;
                            progressBar.setVisibility(View.GONE);
                        }
                        if (lprice != 2147483647) {
                            textLowPrice.setText(String.format("%,d", lprice) + "원");
                        } else {
                            textLowPrice.setText("검색 결과 없음");
                            Toast.makeText(MainActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        textLowPrice.setVisibility(View.VISIBLE);
                        Log.e("listItemPositon",
                                customLayoutManager.findLastCompletelyVisibleItemPosition() + "");
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }


            @Override
            public void onFailure(Call<SearchDataList> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "에러가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
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
                clearData();
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
            progressBar.setVisibility(View.GONE);
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


