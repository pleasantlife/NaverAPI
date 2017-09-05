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
import java.util.ArrayList;
import java.util.List;

import static com.kimjinhwan.android.naverapi.Util.Client.CLIENT_ID;
import static com.kimjinhwan.android.naverapi.Util.Client.CLIENT_SECRET;
import static com.kimjinhwan.android.naverapi.Util.Client.SERVER_URL;

/**
 * Created by XPS on 2017-09-05.
 */


//네트워크 통신은 Main Thread에서 할 수 없기 때문에 별도의 Thread를 상속받은 CustomThread 클래스를 만들었다.
public class CustomThread extends Thread {

    List<Items> itemList;
    String queryString, buildDate;
    TextView responseText, textLowPrice;
    AdapterRecycler adapterRecycler;
    public static long lowestPrice = 2000000000;

    public CustomThread(String queryString, TextView responseText, TextView textLowPrice, AdapterRecycler adapterRecycler) {
        this.queryString = queryString;
        this.responseText = responseText;
        this.adapterRecycler = adapterRecycler;
        this.textLowPrice = textLowPrice;
    }

    @Override
    public void run() {
        super.run();
        new AsyncTask<Void, Void, String>() {


            @Override
            protected String doInBackground(Void... voids) {
                String text = null;
                itemList = new ArrayList<>();
                try {
                    text = URLEncoder.encode(queryString, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String complete = "";
                try {
                    URL url = new URL(SERVER_URL + text);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
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
                        item.setTitle(jsonObject.get("title").getAsString());
                        item.setImage(jsonObject.get("image").getAsString());
                        item.setLprice(jsonObject.get("lprice").getAsString());

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

            @Override
            protected void onPostExecute(String complete) {
                super.onPostExecute(complete);
                adapterRecycler.notifyDataSetChanged();
                textLowPrice.setText(lowestPrice+"원");
                textLowPrice.setVisibility(View.VISIBLE);
                Log.e("lowestPrice", lowestPrice+"");
            }
        }.execute();
    }
}
