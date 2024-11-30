package eumsun.backend.controller.oauthController;

import eumsun.backend.domain.SsoType;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.toService.DecodeOpenId;
import eumsun.backend.dto.request.Oauth2JoinDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.toService.JoinUserOIDCDto;
import eumsun.backend.service.openId.OIDCService;
import eumsun.backend.service.OpenIdService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static eumsun.backend.service.openId.OpenIdUtil.KAKAO_ISS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class KakaoController {
    private final OpenIdService openIdService;
    private final OIDCService oidcService;

    @Value("${openid.kakao}")
    private String kakaoAud;

    @PostMapping("/login")
    public API<TokenDto> kakaoLogin(@RequestBody String kakaoJson) {
        Claims claims = decodeToken(kakaoJson);
        TokenDto token = oidcService.oidcLogin(claims);

        return new API<>(token, APIServerMessage.요청_성공);
    }

    @PostMapping("/signup")
    public API<String> kakaoJoin(@RequestBody Oauth2JoinDto dto) {
        Claims claims = decodeToken(dto.getToken());
        JoinUserOIDCDto oidcDto = new JoinUserOIDCDto(claims, dto.getUserType(), SsoType.KAKAO);
        APIMessage apiMessage = oidcService.oidcJoin(oidcDto);

        return new API<>(apiMessage);
    }

    private Claims decodeToken(String token) {
        return openIdService.decodeToken(new DecodeOpenId(
                token,
                kakaoAud,
                KAKAO_ISS,
                SsoType.KAKAO.name()));
    }
}
