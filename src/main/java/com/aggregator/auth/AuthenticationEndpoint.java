package com.aggregator.auth;

import com.aggregator.config.AggregatorApp;
import io.jsonwebtoken.Jwts;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Path("/authentication")
public class AuthenticationEndpoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {
        try {
            authenticate(credentials.getUsername(), credentials.getPassword());
            String token = issueToken(credentials.getUsername());

            return Response.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    private void authenticate(String username, String password) throws Exception {
        System.out.println("logged in with valid credentials");
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