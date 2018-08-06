package com.mcbarin.spotifypreview.models;

import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    @SerializedName("tracks")
    private Track tracks;

    public Track getTracks() {
        return tracks;
    }

    public void setTracks(Track tracks) {
        this.tracks = tracks;
    }
}
