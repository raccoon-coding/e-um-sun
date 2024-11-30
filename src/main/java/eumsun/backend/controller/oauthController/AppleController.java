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

import static eumsun.backend.service.openId.OpenIdUtil.APPLE_ISS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/apple")
public class AppleController {
    private final OpenIdService openIdService;
    private final OIDCService oidcService;

    @Value("${openid.apple}")
    private String appleAud;

    @PostMapping("/login")
    public API<TokenDto> appleLogin(@RequestBody String appleJson) {
        Claims claims = decodeToken(appleJson);
        TokenDto token = oidcService.oidcLogin(claims);

        return new API<>(token, APIServerMessage.요청_성공);
    }

    @PostMapping("/signup")
    public API<String> appleJoin(@RequestBody Oauth2JoinDto dto) {
        Claims claims = decodeToken(dto.getToken());
        JoinUserOIDCDto oidcDto = new JoinUserOIDCDto(claims, dto.getUserType(), SsoType.APPLE);
        APIMessage apiMessage = oidcService.oidcJoin(oidcDto);

        return new API<>(apiMessage);
    }

    private Claims decodeToken(String token) {
        return openIdService.decodeToken(new DecodeOpenId(
                token,
                appleAud,
                APPLE_ISS,
                SsoType.APPLE.name()));
    }
}
