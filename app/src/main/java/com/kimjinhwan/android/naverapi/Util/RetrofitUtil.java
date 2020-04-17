package com.kimjinhwan.android.naverapi.Util;

import android.util.Log;

import com.kimjinhwan.android.naverapi.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    public static String NAVER_URL = "https://openapi.naver.com/v1/search/";


    public void getRetrofit(){

        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor()).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(NAVER_URL).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();

        NaverShoppingSearchService naverShoppingSearchService = retrofit.create(NaverShoppingSearchService.class);
    }

    private HttpLoggingInterceptor loggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("okhttp : ", message+"");
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

}
