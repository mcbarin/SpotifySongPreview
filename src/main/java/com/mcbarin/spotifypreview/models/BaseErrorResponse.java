package com.mcbarin.spotifypreview.models;

public class BaseErrorResponse implements BaseResponse {

    private ErrorModel error;

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }
}
