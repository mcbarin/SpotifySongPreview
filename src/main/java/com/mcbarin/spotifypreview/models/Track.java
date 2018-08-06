package com.mcbarin.spotifypreview.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Track {
    @SerializedName("href")
    private String href;

    @SerializedName("items")
    private List<TrackItem> items;

    @SerializedName("limit")
    private int limit;

    @SerializedName("next")
    private String next;

    @SerializedName("previous")
    private String previous;

    @SerializedName("total")
    private int total;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<TrackItem> getItems() {
        return items;
    }

    public void setItems(List<TrackItem> items) {
        this.items = items;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
