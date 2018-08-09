package com.mcbarin.spotifypreview;

import com.mcbarin.spotifypreview.models.SearchResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;

import java.util.Objects;

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

            String previewResponseMessage = null;

            if (searchResponse != null) {

                if (searchResponse.getTracks().getItems().size() != 0) {
                    String previewUrl = searchResponse.getTracks().getItems().get(0).getPreviewUrl();

                    // Song found and it has a preview.
                    if (previewUrl != null) {
                        req.response()
                            .putHeader("content-type", "text/html")
                            .end(getPreviewResponse(previewUrl));
                    } else {
                        // Song found but there is no preview.
                        previewResponseMessage = "Şarkıya ait bir ses örneği bulunamadı";
                    }
                } else {
                    // Song not found.
                    previewResponseMessage = "Şarkı bulunamadı.";
                }
            } else {
                previewResponseMessage = "Bir hata oluştu.";
            }

            if (!Objects.equals(previewResponseMessage, null)) {
                req.response().putHeader("content-type", "text/html; charset=utf-8").end(previewResponseMessage);
            }
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    }

    /**
     * @param url
     * @return HTML content of an audio player.
     */
    private static String getPreviewResponse(String url) {
        return String.format("<html><head><meta name=\"viewport\" content=\"width=device-width\"></head><body><video controls=\"\" autoplay=\"\" name=\"media\"><source src=\"%s\" type=\"audio/mpeg\"></video></body></html>", url);
    }
}
