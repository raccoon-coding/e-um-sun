package eumsun.backend.config.jwt.openId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenIdTokenParser {
    private static final String IDENTITY_TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;

    private final ObjectMapper objectMapper;

    public String getDecodeToken(final String token) {
        final String encodedHeader = token.split(IDENTITY_TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
        return new String(Base64.getDecoder().decode(encodedHeader));
    }

    public Map<String, Object> getMapPayloads(final String decodeToken) {
        try {
            return objectMapper.readValue(decodeToken, new TypeReference<>() {});
        } catch (JsonMappingException e) {
            throw new RuntimeException("appleToken 값이 jwt 형식인지, 값이 정상적인지 확인해주세요.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("디코드된 헤더를 Map 형태로 분류할 수 없습니다. 헤더를 확인해주세요.");
        }
    }

    public Claims extractClaims(final String token, final PublicKey publicKey) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 jwt 타입");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("비어있는 jwt");
        } catch (JwtException e) {
            throw new JwtException("jwt 검증 or 분석 오류");
        }
    }

    public void verifyOpenIdToken(final String aud, final String iss, final String token){
        try {
            Jwts.parser()
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .build()
                    .parseSignedClaims(token);
        }
        catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 Id Token 입니다.");
        }
        catch (Exception e){
            throw new IllegalArgumentException("잘못된 Id Token 입니다.");
        }
    }
}
