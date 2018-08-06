package com.mcbarin.spotifypreview;

import com.mcbarin.spotifypreview.models.SearchResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {

        Router router = Router.router(vertx);
        router.route("/rest/songfinder").handler(routingContext -> {

            HttpServerRequest req = routingContext.request();

            // Retrieving query parameters from url.
            String songName = req.getParam("songname");
            String token = req.getParam("token");

            SearchResponse searchResponse = NetworkManager.getSearchResults(songName, token);
            if (searchResponse != null) {
                String previewUrl = searchResponse.getTracks().getItems().get(0).getPreviewUrl();

                req.response()
                    .putHeader("content-type", "text/html")
                    .end(getPreviewResponse(previewUrl));
            } else {
                req.response().putHeader("content-type", "text/html").end("Şarkı bulunamadı.");
            }

        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

//        http://localhost:8080/rest/songfinder?songname=californication&token=BQCfo0xRZrlgxDWfCslsU52GCQ-cQ6VnXCFQ4-edgs9q3zGLItmPKxnAeHxwscEBx7sJKAcFkVqlFABSWbA
    }

    /**
     *
     * @param url
     * @return HTML content of an audio player.
     */
    private static String getPreviewResponse(String url) {
        return String.format("<html><head><meta name=\"viewport\" content=\"width=device-width\"></head><body><video controls=\"\" autoplay=\"\" name=\"media\"><source src=\"%s\" type=\"audio/mpeg\"></video></body></html>", url);
    }
}
