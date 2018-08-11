package com.mcbarin.spotifypreview;


import com.mcbarin.spotifypreview.models.BaseResponse;

public interface NetworkListener {

    void onSuccess(BaseResponse response);

    void onFail(BaseResponse response);
}
