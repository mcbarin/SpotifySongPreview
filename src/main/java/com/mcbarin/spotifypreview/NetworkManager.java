package com.mcbarin.spotifypreview;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mcbarin.spotifypreview.models.SearchResponse;

public class NetworkManager {

    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private static final String BASE_URL = "https://api.spotify.com/v1";

    static SearchResponse getSearchResults(String songname, String token) {

        String uri = "search";
        String searchUrl = String.format("%s/%s?q=%s&type=track&market=TR&limit=10&offset=1", BASE_URL, uri, songname);

        try {
            HttpResponse<String> response = Unirest.get(searchUrl)
                .header("Authorization", String.format("Bearer %s", token))
                .asString();

            Gson gson = new GsonBuilder().create();

            // Deserialize response body and return it.
            return gson.fromJson(response.getBody(), SearchResponse.class);


        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }
}

