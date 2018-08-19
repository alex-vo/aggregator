package com.aggregator.auth;

import com.aggregator.auth.domain.GoogleAccessTokenResponse;
import com.aggregator.config.AggregatorApp;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Path("authentication")
public class AuthenticationEndpoint {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {
        try {
            authenticate(credentials.getUsername(), credentials.getPassword());
            String token = issueToken(credentials.getUsername());

            return Response.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .build();
        } catch (Exception e) {
            log.error("Failed to authorize", e);
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("googleOAuth2Callback")
    public Response authenticateWithGoogle(
            @QueryParam("state") String state,
            @QueryParam("code") String code
    ) {
        if (!StringUtils.equals(state, AggregatorApp.properties.getProperty("google.state.passthrough.value"))) {
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid passthrough value").build();
        }

        try {
            GoogleAccessTokenResponse googleAccessTokenResponse = RestClient.getInstance().retrieveGoogleAccessToken(code);
            String token = issueToken(googleAccessTokenResponse.getAccessToken());
            return Response.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .entity("logged in")
                    .build();
        } catch (Exception e) {
            log.error("Failed to authorize with Google", e);
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    private void authenticate(String username, String password) throws Exception {
        log.info("logged in with valid credentials");
    }

    private String issueToken(String username) throws IOException, GeneralSecurityException {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(
                        KeyUtil.loadKeyStore(
                                new File(AggregatorApp.properties.getProperty("keystore.file")),
                                AggregatorApp.properties.getProperty("keystore.password")
                        ).getKey("jetty", AggregatorApp.properties.getProperty("keystore.password").toCharArray()))
                .compact();
    }
}