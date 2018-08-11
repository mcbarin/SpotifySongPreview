package com.mcbarin.spotifypreview.models;

import com.google.gson.annotations.SerializedName;

public class SearchResponse implements BaseResponse {

    @SerializedName("tracks")
    private Track tracks;

    public Track getTracks() {
        return tracks;
    }

    public void setTracks(Track tracks) {
        this.tracks = tracks;
    }
}
