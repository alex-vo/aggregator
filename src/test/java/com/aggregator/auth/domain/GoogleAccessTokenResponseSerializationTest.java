package com.aggregator.auth.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GoogleAccessTokenResponseSerializationTest {

    @Test
    public void testDeserialize() throws IOException {
        String raw = "{\n" +
                "    \"access_token\": \"ya29.Glv-Bc4qcedPJ6mY8SF1dqbzqU9dJ8sTdQyB2KLSKTUxLhh6bxG_j5tKBu6cHleB745X8kjrJO4Eo58JAeB7CAlAHOnyohZnz6RUn5P99I5vsv1EC8CjSHEhnQ8g\",\n" +
                "    \"expires_in\": 3463,\n" +
                "    \"scope\": \"https://www.googleapis.com/auth/drive.metadata.readonly\",\n" +
                "    \"unknown\": \"ignored\",\n" +
                "    \"token_type\": \"Bearer\"\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        GoogleAccessTokenResponse result = mapper.readValue(raw, GoogleAccessTokenResponse.class);
        assertEquals(result.getAccessToken(), "ya29.Glv-Bc4qcedPJ6mY8SF1dqbzqU9dJ8sTdQyB2KLSKTUxLhh6bxG_j5tKBu6cHleB745X8kjrJO4Eo58JAeB7CAlAHOnyohZnz6RUn5P99I5vsv1EC8CjSHEhnQ8g");
        assertEquals(result.getExpiresIn(), 3463L);
        assertEquals(result.getScope(), "https://www.googleapis.com/auth/drive.metadata.readonly");
        assertEquals(result.getTokenType(), "Bearer");
    }

}
