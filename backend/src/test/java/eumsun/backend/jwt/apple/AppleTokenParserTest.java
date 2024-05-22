package eumsun.backend.jwt.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import eumsun.backend.config.jwt.openId.OpenIdTokenParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class AppleTokenParserTest {

    private final OpenIdTokenParser appleTokenParser = new OpenIdTokenParser(new ObjectMapper());

    @Test
    public void 애플_토큰_헤더_파싱_테스트() throws NoSuchAlgorithmException {
        // given
        Date now = new Date();
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        String appleToken = Jwts.builder()
                .header().add("kid", "kid 값")
                .and()
                .claim("email", "test@test.com")
                .issuer("https://appleid.apple.com")
                .issuedAt(now)
                .audience().add("aud 값")
                .and()
                .subject("sub_test")
                .expiration(new Date(now.getTime() + 60 * 60 * 1000))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        // when
        String decodeToken = appleTokenParser.getDecodeToken(appleToken);
        Map<String, Object> headers = appleTokenParser.getMapPayloads(decodeToken);

        // given
        assertThat(headers).containsKeys("alg", "kid");
    }

    @Test
    public void 애플_클레임_파싱_테스트() throws NoSuchAlgorithmException {
        // given
        Date now = new Date();
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA")
                .generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String appleToken = Jwts.builder()
                .header().add("kid", "kid 값")
                .and()
                .claim("email", "test@test.com")
                .issuer("https://appleid.apple.com")
                .issuedAt(now)
                .audience().add("aud 값")
                .and()
                .subject("sub_test")
                .expiration(new Date(now.getTime() + 60 * 60 * 1000))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        // when
        Claims claims = appleTokenParser.extractClaims(appleToken, publicKey);

        // then
        assertThat(claims.get("email")).isEqualTo("test@test.com");
    }
}