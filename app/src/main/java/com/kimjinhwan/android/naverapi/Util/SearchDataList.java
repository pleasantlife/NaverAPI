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
    private String total;
    private String start;
    private String display;

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
