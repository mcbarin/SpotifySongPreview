package com.mcbarin.spotifypreview.models;

import com.google.gson.annotations.SerializedName;

public class TrackItem {

    @SerializedName("name")
    private String name;

    @SerializedName("preview_url")
    private String previewUrl;

    @SerializedName("type")
    private String type;

    @SerializedName("uri")
    private String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
