package com.kimjinhwan.android.naverapi.Util;

/**
 * Created by XPS on 2017-09-05.
 */

public class Client {

    public static final String CLIENT_ID = "OzYyCwp8a0JpBJiKXycC";
    public static final String CLIENT_SECRET = "SszZOHXjYS";
    public static ThreadLocal<String> SERVER_URL = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "https://openapi.naver.com/v1/search/shop?display=" + ITEM_VALUE + "&query=";
        }
    };
    public static int ITEM_VALUE = 10;
}
