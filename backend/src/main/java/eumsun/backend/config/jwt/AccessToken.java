package eumsun.backend.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static eumsun.backend.config.util.JwtUtil.USER_EMAIL;

public interface AccessToken extends JwtCreator {

    Authentication getAuthentication(String token);

    default String getAccessTokenUserPk(String token) {
        return verifyAccess(token)
                .getPayload()
                .get(USER_EMAIL)
                .toString();
    }

    default Boolean validateAccessToken(String token) {

        try {
            Jws<Claims> claims = verifyAccess(token);
            return !claims.getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
