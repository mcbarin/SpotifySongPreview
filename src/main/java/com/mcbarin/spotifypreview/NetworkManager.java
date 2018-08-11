package com.mcbarin.spotifypreview;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mcbarin.spotifypreview.models.BaseErrorResponse;
import com.mcbarin.spotifypreview.models.SearchResponse;

class NetworkManager {

    private static NetworkManager ourInstance = new NetworkManager();

    static NetworkManager getInstance() {
        return ourInstance;
    }

    private final String BASE_URL = "https://api.spotify.com/v1";

    void getSearchResults(String songname, String token, NetworkListener listener) {

        String uri = "search";
        String searchUrl = String.format("%s/%s", BASE_URL, uri);

        try {
            HttpResponse<String> response = Unirest.get(searchUrl)
                .header("Authorization", String.format("Bearer %s", token))
                .queryString("q", songname)
                .queryString("type", "track")
                .queryString("market", "TR")
                .queryString("limit", "10")
                .queryString("offset", "5")
                .asString();


            Gson gson = new GsonBuilder().create();

            if (isSuccessStatusCode(response.getStatus())) {
                SearchResponse searchResponse = gson.fromJson(response.getBody(), SearchResponse.class);
                listener.onSuccess(searchResponse);
            } else {
                BaseErrorResponse errorResponse = gson.fromJson(response.getBody(), BaseErrorResponse.class);
                listener.onFail(errorResponse);
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            listener.onFail(null);
        }
    }

    // 2xx status codes are accepted as successful requests.
    private boolean isSuccessStatusCode(int status) {
        return (status >= 200 && status < 300);
    }
}

