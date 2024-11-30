package eumsun.backend.controller.oauthController;

import eumsun.backend.domain.SsoType;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.toService.DecodeOpenId;
import eumsun.backend.dto.request.Oauth2JoinDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.toService.JoinUserOIDCDto;
import eumsun.backend.service.OpenIdService;
import eumsun.backend.service.openId.OIDCService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static eumsun.backend.service.openId.OpenIdUtil.GOOGLE_ISS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/google")
public class GoogleController {
    private final OpenIdService openIdService;
    private final OIDCService oidcService;

    @Value("${openid.google}")
    private String googleAud;

    @PostMapping("/login")
    public API<TokenDto> googleLogin(@RequestBody String googleJson) {
        Claims claims = decodeToken(googleJson);
        TokenDto token = oidcService.oidcLogin(claims);

        return new API<>(token, APIServerMessage.요청_성공);
    }

    @PostMapping("/signup")
    public API<String> googleJoin(@RequestBody Oauth2JoinDto dto) {
        Claims claims = decodeToken(dto.getToken());
        JoinUserOIDCDto oidcDto = new JoinUserOIDCDto(claims, dto.getUserType(), SsoType.GOOGLE);
        APIMessage apiMessage = oidcService.oidcJoin(oidcDto);

        return new API<>(apiMessage);
    }

    private Claims decodeToken(String token) {
        return openIdService.decodeToken(new DecodeOpenId(
                token,
                googleAud,
                GOOGLE_ISS,
                SsoType.GOOGLE.name()));
    }
}
