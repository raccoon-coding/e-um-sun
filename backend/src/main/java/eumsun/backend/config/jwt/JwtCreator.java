package eumsun.backend.config.jwt;

import eumsun.backend.dto.parameter.CreateTokenDto;
import eumsun.backend.dto.response.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static eumsun.backend.config.util.JwtUtil.HEADER;
import static eumsun.backend.config.util.JwtUtil.ONE_DAY;
import static eumsun.backend.config.util.JwtUtil.REFRESH_COUNT;
import static eumsun.backend.config.util.JwtUtil.SECRET_KEY;
import static eumsun.backend.config.util.JwtUtil.SUBJECT_ACCESS;
import static eumsun.backend.config.util.JwtUtil.SUBJECT_REFRESH;
import static eumsun.backend.config.util.JwtUtil.TOKEN_PREFIX;
import static eumsun.backend.config.util.JwtUtil.USER_EMAIL;

public interface JwtCreator {

    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SECRET_KEY));

    default TokenDto createToken(CreateTokenDto tokenDto){

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ONE_DAY);
        Date refreshTokenExpiresIn = new Date(now + ONE_DAY);

        String accessToken = createAccessToken(tokenDto, accessTokenExpiresIn);

        String refreshToken = createRefreshToken(tokenDto, refreshTokenExpiresIn);

        return TokenDto.builder()
                .grantType(TOKEN_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    default Jws<Claims> verifyAccess(String jwt){
        return Jwts.parser()
                .verifyWith(key)
                .requireSubject(SUBJECT_ACCESS)
                .build()
                .parseSignedClaims(jwt);
    }

    default Jws<Claims> verifyRefresh(String jwt){
        return Jwts.parser()
                .verifyWith(key)
                .requireSubject(SUBJECT_REFRESH)
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

        return null;
    }

    private String createAccessToken(CreateTokenDto tokenDto, Date accessTokenExpiresIn) {
        return Jwts.builder()
                .subject(SUBJECT_ACCESS)
                .claim(USER_EMAIL, tokenDto.getUserEmail())
                .expiration(accessTokenExpiresIn)
                .signWith(key)
                .compact();
    } // access 토큰은 유저 email로 정의한다.

    private String createRefreshToken(CreateTokenDto tokenDto, Date refreshTokenExpiresIn) {
        return Jwts.builder()
                .subject(SUBJECT_REFRESH)
                .claim(USER_EMAIL, tokenDto.getUserEmail())
                .claim(REFRESH_COUNT, tokenDto.getRefreshCount() + 1)
                .expiration(refreshTokenExpiresIn)
                .signWith(key)
                .compact();
    }
}
