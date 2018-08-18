package com.aggregator.config;

import com.aggregator.FirstService;
import com.aggregator.auth.AuthenticationEndpoint;
import com.aggregator.auth.AuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class AggregatorApp extends ResourceConfig {

    public static final String PROPERTIES_FILE = "local.properties";
    public static Properties properties = new Properties();

    private Properties readProperties() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                log.error(String.format("Failed to load %s file: %s", PROPERTIES_FILE, e.getMessage()), e);
            }
        }

        return properties;
    }

    public AggregatorApp() {
        readProperties();
        register(AuthenticationEndpoint.class);
        register(FirstService.class);
        register(AuthenticationFilter.class);
    }

}
