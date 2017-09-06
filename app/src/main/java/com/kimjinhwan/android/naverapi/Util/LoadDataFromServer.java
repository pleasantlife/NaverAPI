package com.kimjinhwan.android.naverapi.Util;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kimjinhwan.android.naverapi.AdapterRecycler;
import com.kimjinhwan.android.naverapi.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kimjinhwan.android.naverapi.Util.Client.CLIENT_ID;
import static com.kimjinhwan.android.naverapi.Util.Client.CLIENT_SECRET;
import static com.kimjinhwan.android.naverapi.Util.Client.SERVER_URL;

/**
 * Created by XPS on 2017-09-05.
 */


//네트워크 통신은 Main Thread에서 할 수 없기 때문에 별도의 Thread를 상속받은 커스텀 스레드 클래스를 만들었다.
public class LoadDataFromServer extends Thread {

    List<Items> itemList;
    String queryString, buildDate;
    TextView responseText, textLowPrice, textQueryTime;
    AdapterRecycler adapterRecycler;

    public static long lowestPrice = 2000000000;

    public LoadDataFromServer(String queryString, TextView responseText, TextView textLowPrice, TextView textQueryTime, AdapterRecycler adapterRecycler) {
        this.queryString = queryString;
        this.responseText = responseText;
        this.adapterRecycler = adapterRecycler;
        this.textLowPrice = textLowPrice;
        this.textQueryTime = textQueryTime;
    }

    @Override
    public void run() {
        super.run();
        new AsyncTask<Void, Void, String>() {

            //여기는 메인 스레드가 아니므로 UI를 처리할 수 없음.(setText 불가!)
            @Override
            protected String doInBackground(Void... voids) {
                String text = null;
                String displayValue = null;
                itemList = new ArrayList<>();
                try {
                    //네이버에서 검색어를 반드시 UTF-8로 인코딩할 것을 명시함.
                    text = URLEncoder.encode(queryString, "UTF-8");
                    //displayValue = URLEncoder.encode("display="+20, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String complete = "";
                try {
                    URL url = new URL(SERVER_URL.get() + text);
                    Log.e("url===", url+"");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    //네이버에서 부여받은 클라이언트 아이디와 비밀번호를 받아옴.
                    connection.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
                    connection.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);
                    int responseCode = connection.getResponseCode();
                    BufferedReader br;
                    if(responseCode == 200){
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    } else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                        Log.e("inputLine", inputLine);
                    }
                    complete = response.toString();
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(complete);
                    buildDate = element.getAsJsonObject().get("lastBuildDate").getAsString();
                    JsonArray jsonArray = element.getAsJsonObject().getAsJsonArray("items");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        Items item = new Items();
                        String rawTitle = jsonObject.get("title").getAsString();
                        String halfTitle = rawTitle.replace("<b>","");
                        String title = halfTitle.replace("</b>","");
                        item.setTitle(title);
                        item.setImage(jsonObject.get("image").getAsString());
                        item.setLprice(jsonObject.get("lprice").getAsString());

                        /*
                         *  검색 결과 내에서 가장 저렴한 가격을 알아내기 위해 for문을 돌면서 비교함.
                         *
                         */
                        if(lowestPrice >  Long.parseLong(jsonObject.get("lprice").getAsString())){
                            lowestPrice = Long.parseLong(jsonObject.get("lprice").getAsString());
                        }

                        item.setHprice(jsonObject.get("hprice").getAsString());
                        item.setMallName(jsonObject.get("mallName").getAsString());
                        item.setProductId(jsonObject.get("productId").getAsString());
                        itemList.add(item);
                        adapterRecycler.setData(itemList);
                    }
                    br.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return complete;
            }

            //UI 처리는 여기에서 하도록 하여 메인 스레드(UI 스레드)에서 UI의 변경을 처리하도록 해야 함.
            @Override
            protected void onPostExecute(String complete) {
                super.onPostExecute(complete);
                adapterRecycler.notifyDataSetChanged();
                String realDate = buildDate.replace("+0900","");
                textQueryTime.setText(realDate);
                textQueryTime.setVisibility(View.VISIBLE);
                textLowPrice.setText(lowestPrice+"원");
                textLowPrice.setVisibility(View.VISIBLE);
                Log.e("lowestPrice", lowestPrice+"");
            }
        }.execute();
    }
}
