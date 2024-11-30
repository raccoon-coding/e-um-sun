package eumsun.backend.config.jwt;

import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.exception.controller.InvalidRefreshTokenException;
import eumsun.backend.exception.controller.OverRefreshCountException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Date;

import static eumsun.backend.config.util.JwtUtil.MAX_REFRESH;
import static eumsun.backend.config.util.JwtUtil.REFRESH_COUNT;
import static eumsun.backend.config.util.JwtUtil.SUBJECT_REFRESH;
import static eumsun.backend.config.util.JwtUtil.USER_UUID;

public interface RefreshToken extends JwtCreator {
    default String getRefreshUserPk(String token) {
        return verify(token, SUBJECT_REFRESH)
                .getPayload()
                .get(USER_UUID)
                .toString();
    }

    default Integer getRefreshCount(String token) {
        Integer count = (Integer) verify(token, SUBJECT_REFRESH)
                .getPayload()
                .get(REFRESH_COUNT);
        if(count > MAX_REFRESH) {
            throw new OverRefreshCountException(APIErrorMessage.토큰_만료.getMessage());
        }
        return count;
    }

    default Boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = verify(token, SUBJECT_REFRESH);
            return !claims.getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            throw new InvalidRefreshTokenException(APIErrorMessage.다른_토큰.getMessage());
        }
    }
}
