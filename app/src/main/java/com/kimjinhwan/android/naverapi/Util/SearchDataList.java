package com.kimjinhwan.android.naverapi.Util;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XPS on 2017-12-02.
 */

public class SearchDataList {

    @SerializedName("items")
    private List<Items> items;
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }
}
