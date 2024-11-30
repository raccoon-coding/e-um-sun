package eumsun.backend.config.jwt;

import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.exception.controller.RefreshTokenRedirect;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static eumsun.backend.config.util.JwtUtil.SUBJECT_ACCESS;
import static eumsun.backend.config.util.JwtUtil.USER_UUID;

public interface AccessToken extends JwtCreator {
    Authentication getAuthentication(String token, Date iat);

    default String getAccessTokenUserPk(String token) {
        return verify(token, SUBJECT_ACCESS)
                .getPayload()
                .get(USER_UUID)
                .toString();
    }

    default Boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = verify(token, SUBJECT_ACCESS);
            return !claims.getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            throw new RefreshTokenRedirect(APIErrorMessage.토큰_재로그인.getMessage());
        }
    }

    default Date getIat(String token) {
        Jws<Claims> claims = verify(token, SUBJECT_ACCESS);
        return claims.getPayload().getIssuedAt();
    }
}
