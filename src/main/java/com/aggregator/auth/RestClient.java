package com.aggregator.auth;

import com.aggregator.auth.domain.GoogleAccessTokenResponse;
import com.aggregator.config.AggregatorApp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;

public class RestClient {

    public static final String GOOGLE_ACCESS_TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";

    private static RestClient INSTANCE;

    private Client client;

    private RestClient() {
        client = ClientBuilder.newClient();
    }

    public static RestClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RestClient();
        }
        return INSTANCE;
    }

    public GoogleAccessTokenResponse retrieveGoogleAccessToken(String code) {
        Invocation.Builder requestBuilder = client
                .target(GOOGLE_ACCESS_TOKEN_URL)
                .request();
        Form form = new Form();
        form.param("code", code);
        form.param("client_id", AggregatorApp.properties.getProperty("google.client.id"));
        form.param("client_secret", AggregatorApp.properties.getProperty("google.client.secret"));
        form.param("redirect_uri", AggregatorApp.properties.getProperty("google.redirect.uri"));
        form.param("grant_type", "authorization_code");

        return requestBuilder.post(Entity.form(form), GoogleAccessTokenResponse.class);
    }

}
