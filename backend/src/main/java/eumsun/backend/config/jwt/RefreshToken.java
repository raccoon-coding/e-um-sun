package eumsun.backend.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Date;

import static eumsun.backend.config.util.JwtUtil.REFRESH_COUNT;
import static eumsun.backend.config.util.JwtUtil.USER_EMAIL;

public interface RefreshToken extends JwtCreator {

    default String getRefreshUserPk(String token) {

        return verifyRefresh(token)
                .getPayload()
                .get(USER_EMAIL)
                .toString();
    }

    default Integer getRefreshCount(String token) {

        return (Integer) verifyRefresh(token)
                .getPayload()
                .get(REFRESH_COUNT);
    }

    default Boolean validateRefreshToken(String token) {

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
