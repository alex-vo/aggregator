package com.aggregator.auth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

public class KeyUtil {

    public static KeyStore loadKeyStore(File keystoreFile, String password) throws IOException, GeneralSecurityException {
        if (null == keystoreFile) {
            throw new IllegalArgumentException("Keystore url may not be null");
        }

        URI keystoreUri = keystoreFile.toURI();
        URL keystoreUrl = keystoreUri.toURL();
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream is = null;
        try {
            is = keystoreUrl.openStream();
            keystore.load(is, null == password ? null : password.toCharArray());
        } finally {
            if (null != is) {
                is.close();
            }
        }

        return keystore;
    }

}
