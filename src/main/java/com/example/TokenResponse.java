package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
public class TokenResponse {
    String token_type;
    int expires_in;

    int ext_expires_in;

    String access_token;

    @JsonProperty("token_type")
    public String getTokenType() {
        return this.token_type; }

    public void setTokenType(String token_type) {
        this.token_type = token_type; }

    @JsonProperty("expires_in")
    public int getExpiresIn() {
        return this.expires_in; }

    public void setExpiresIn(int expires_in) {
        this.expires_in = expires_in; }

    @JsonProperty("ext_expires_in")
    public int getExtExpiresIn() {
        return this.ext_expires_in; }

    public void setExtExpiresIn(int ext_expires_in) {
        this.ext_expires_in = ext_expires_in; }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return this.access_token; }

    public void setAccessToken(String access_token) {
        this.access_token = access_token; }


}
