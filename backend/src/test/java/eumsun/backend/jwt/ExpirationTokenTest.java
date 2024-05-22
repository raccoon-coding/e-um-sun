package eumsun.backend.jwt;

import eumsun.backend.config.jwt.JwtProvider;
import eumsun.backend.dto.parameter.CreateTokenDto;
import eumsun.backend.dto.response.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static eumsun.backend.config.util.JwtUtil.ONE_DAY;

@SpringBootTest
class ExpirationTokenTest {

    private final String targetEmail = "parent@gmail.com";

    private final JwtProvider jwtProvider;

    @Autowired
    ExpirationTokenTest(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    private TokenDto createTokenDto() {
        CreateTokenDto tokenDto = CreateTokenDto.builder()
                .userEmail(targetEmail)
                .refreshCount(0)
                .build();
        return jwtProvider.createToken(tokenDto);
    }

    @DisplayName("토큰에 저장된 이메일이 정확하게 실려있고, 읽어지는지 확인")
    @Test
    public void createToken() {

        TokenDto token = createTokenDto();

        String accessToken = token.getAccessToken();

        Jws<Claims> accessClaims = jwtProvider.verifyAccess(accessToken);

        Assertions.assertThat(accessClaims.getPayload().get("email")).isEqualTo(targetEmail);
    }

    @DisplayName("만료된 토큰을 거르는지 확인")
    @Test
    public void validAccessToken() {

        long now = (new Date()).getTime();

        TokenDto token = createTokenDto();

        String accessToken = token.getAccessToken();

        Jws<Claims> accessClaims = jwtProvider.verifyAccess(accessToken);

        Date expiredDate = new Date(now + ONE_DAY + 10_000);

        Assertions.assertThat(!accessClaims.getPayload()
                .getExpiration()
                .before(expiredDate))
                .isEqualTo(false);
    }

    @DisplayName("refresh 토큰 확인하기")
    @Test
    public void validRefreshToken() {

        TokenDto token = createTokenDto();

        String refreshToken = token.getRefreshToken();

        Jws<Claims> accessClaims = jwtProvider.verifyRefresh(refreshToken);

        Assertions.assertThat(accessClaims.getPayload().get("email").toString()).isEqualTo(targetEmail);
    }

    @DisplayName("refresh 토큰으로 access 토큰 재발급 받기")
    @Test
    public void reGenerationToken() {

        TokenDto token = createTokenDto();

        String refreshToken = token.getRefreshToken();

        String userEmail = jwtProvider.getRefreshUserPk(refreshToken);
        Integer refreshCount = jwtProvider.getRefreshCount(refreshToken);

        CreateTokenDto tokenDto = CreateTokenDto.builder()
                .userEmail(userEmail)
                .refreshCount(refreshCount)
                .build();

        TokenDto reGenerationToken = jwtProvider.createToken(tokenDto);
        String reGeneration = reGenerationToken.getRefreshToken();
        Integer reGenerationCount = jwtProvider.getRefreshCount(reGeneration);

        Assertions.assertThat(reGenerationCount).isEqualTo(2);
    }
}
