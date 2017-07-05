package com.tma.tctay.models;

/**
 * Created by tctay on 6/14/2017.
 */

public class AccessToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public AccessToken(String access_token, String token_type, String refresh_token, Integer expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
    }

    public AccessToken() {
    }
}
