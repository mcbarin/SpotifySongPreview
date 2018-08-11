package com.mcbarin.spotifypreview;

import com.mcbarin.spotifypreview.models.BaseErrorResponse;
import com.mcbarin.spotifypreview.models.BaseResponse;
import com.mcbarin.spotifypreview.models.SearchResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * Reading Notes for me:
 * https://benramsey.com/blog/2008/05/206-partial-content-and-range-requests/
 * https://www.nurkiewicz.com/2015/06/writing-download-server-part-i-always.html
 */

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {

        Router router = Router.router(vertx);

        router.route(HttpMethod.GET, "/rest/songfinder").handler(this::songFinder);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    }

    private void songFinder(RoutingContext routingContext) {

        HttpServerRequest req = routingContext.request();

        // Retrieving query parameters from url.
        String songName = req.getParam("songname");
        String token = req.getParam("token");

        NetworkManager.getInstance().getSearchResults(songName, token, new NetworkListener() {
            @Override
            public void onSuccess(BaseResponse response) {

                SearchResponse searchResponse = (SearchResponse) response;
                String previewResponseMessage = null;

                if (response != null
                    && ((SearchResponse) response).getTracks() != null
                    && ((SearchResponse) response).getTracks().getItems() != null) {

                    if (searchResponse.getTracks().getItems().size() != 0) {
                        String previewUrl = searchResponse.getTracks().getItems().get(0).getPreviewUrl();

                        // Song found and it has a preview.
                        if (previewUrl != null) {

                            File previewFile = downloadPreviewMusic(previewUrl);

                            if (previewFile != null) {
                                req.response()
                                    .setChunked(true)
                                    .putHeader("content-type", "audio/mpeg")
                                    .putHeader("content-length", "" + previewFile.length())
                                    .sendFile("/tmp/preview.mp3");
                            } else {
                                previewResponseMessage = "Bir hata oluştu.";
                            }
                        } else {
                            // Song found but there is no preview.
                            previewResponseMessage = "Şarkıya ait bir ses örneği bulunamadı";
                        }
                    } else {
                        // Song not found.
                        previewResponseMessage = "Şarkı bulunamadı.";
                    }
                } else {
                    // An Error occurred.
                    previewResponseMessage = "Bir hata oluştu.";
                }

                if (!Objects.equals(previewResponseMessage, null)) {
                    req.response().putHeader("content-type", "text/html; charset=utf-8").end(previewResponseMessage);
                }
            }

            @Override
            public void onFail(BaseResponse response) {
                BaseErrorResponse errorResponse = (BaseErrorResponse) response;
                if (response != null) {
                    req.response().putHeader("content-type", "text/html; charset=utf-8").end(errorResponse.getError().getMessage());
                } else {
                    req.response().putHeader("content-type", "text/html; charset=utf-8").end("Bir hata oluştu.");
                }
            }
        });
    }

    /**
     * @param url
     * @return File object downloaded from parameter url
     */
    private static File downloadPreviewMusic(String url) {
        URLConnection conn;
        try {
            conn = new URL(url).openConnection();
            InputStream is = conn.getInputStream();

            OutputStream outStream = new FileOutputStream(new File("/tmp/preview.mp3"));
            byte[] buffer = new byte[10240];
            int len;
            while ((len = is.read(buffer)) > 0) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();

            return new File("/tmp/preview.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
