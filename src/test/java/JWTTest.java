import com.aggregator.auth.KeyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JWTTest {

    private Properties properties;

    @Before
    public void setUp() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("local.properties");
        properties = new Properties();
        properties.load(inputStream);
    }

    @Test
    public void testTest() throws IOException, GeneralSecurityException {
        String jwt = Jwts.builder()
                .setSubject("leha")
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(
                        KeyUtil.loadKeyStore(
                                new File(properties.getProperty("keystore.file")),
                                properties.getProperty("keystore.password")
                        ).getKey("jetty", properties.getProperty("keystore.password").toCharArray()))
                .compact();

        try {
            Jwts.parser().setSigningKey(
                    KeyUtil.loadKeyStore(
                            new File(properties.getProperty("keystore.file")),
                            properties.getProperty("keystore.password")
                    ).getKey("jetty", properties.getProperty("keystore.password").toCharArray())
            ).parseClaimsJws(jwt.replaceFirst("a", "b"));
            fail();
        } catch (SignatureException e) {
            //all ok
        }

        Jws<Claims> jws = Jwts.parser().setSigningKey(
                KeyUtil.loadKeyStore(
                        new File(properties.getProperty("keystore.file")),
                        properties.getProperty("keystore.password")
                ).getKey("jetty", properties.getProperty("keystore.password").toCharArray())
        ).parseClaimsJws(jwt);
        assertEquals(jws.getBody().getSubject(), "leha");
    }
}
