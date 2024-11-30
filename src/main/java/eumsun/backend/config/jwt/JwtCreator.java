package eumsun.backend.config.jwt;

import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.toService.CreateTokenDto;
import eumsun.backend.exception.controller.NotHaveToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static eumsun.backend.config.util.JwtUtil.HALF_HOUR;
import static eumsun.backend.config.util.JwtUtil.HEADER;
import static eumsun.backend.config.util.JwtUtil.ONE_HOUR;
import static eumsun.backend.config.util.JwtUtil.REFRESH_COUNT;
import static eumsun.backend.config.util.JwtUtil.SUBJECT_ACCESS;
import static eumsun.backend.config.util.JwtUtil.SUBJECT_REFRESH;
import static eumsun.backend.config.util.JwtUtil.TOKEN_PREFIX;
import static eumsun.backend.config.util.JwtUtil.USER_UUID;

public interface JwtCreator {
    SecretKey getKey();

    default TokenDto createToken(CreateTokenDto tokenDto){
        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + HALF_HOUR);
        Date refreshTokenExpiresIn = new Date(now + ONE_HOUR);

        String accessToken = createAccessToken(tokenDto, accessTokenExpiresIn);
        String refreshToken = createRefreshToken(tokenDto, refreshTokenExpiresIn);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    default Jws<Claims> verify(String jwt, String type){
        return Jwts.parser()
                .verifyWith(getKey())
                .requireSubject(type)
                .build()
                .parseSignedClaims(jwt);
    }

    default String getToken(HttpServletRequest request){
        // 헤더에서 토큰 부분을 분리
        String token = request.getHeader(HEADER);

        // 해당 키에 해당하는 헤더가 존재하고, 그 값이 Bearer로 시작한다면 (즉 JWT가 있다면)
        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            // PREFIX 부분을 날리고 JWT만 token에 할당한다.
            return token.substring(TOKEN_PREFIX.length());
        }
        throw new NotHaveToken(APIErrorMessage.토큰_요청.getMessage());
    }

    private String createAccessToken(CreateTokenDto tokenDto, Date tokenExpiresIn) {
        String token = Jwts.builder()
                .subject(SUBJECT_ACCESS)
                .claim(USER_UUID, tokenDto.userId())
                .issuedAt(new Date())
                .expiration(tokenExpiresIn)
                .signWith(getKey())
                .compact();
        return TOKEN_PREFIX + token;
    }

    private String createRefreshToken(CreateTokenDto tokenDto, Date tokenExpiresIn) {
        String token = Jwts.builder()
                .subject(SUBJECT_REFRESH)
                .claim(USER_UUID, tokenDto.userId())
                .claim(REFRESH_COUNT, tokenDto.refreshCount() + 1)
                .expiration(tokenExpiresIn)
                .signWith(getKey())
                .compact();
        return TOKEN_PREFIX + token;
    }
}
