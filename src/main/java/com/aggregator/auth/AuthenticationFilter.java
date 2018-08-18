package com.aggregator.auth;

import com.aggregator.config.AggregatorApp;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null) {
            abortWithUnauthorized(requestContext);
            return;
        }
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
        try {
            validateToken(token);
        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    private void validateToken(String token) throws IOException, GeneralSecurityException {
        Jwts.parser()
                .setSigningKey(
                        KeyUtil.loadKeyStore(
                                new File(AggregatorApp.properties.getProperty("keystore.file")),
                                AggregatorApp.properties.getProperty("keystore.password")
                        ).getKey("jetty", AggregatorApp.properties.getProperty("keystore.password").toCharArray()))
                .parseClaimsJws(token);
    }
}