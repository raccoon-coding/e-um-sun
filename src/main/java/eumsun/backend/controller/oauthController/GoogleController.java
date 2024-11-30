package eumsun.backend.controller.oauthController;

import eumsun.backend.domain.SsoType;
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
    public TokenDto googleLogin(@RequestBody String googleJson) {
        Claims claims = decodeToken(googleJson);

        return oidcService.oidcLogin(claims);
    }

    @PostMapping("/signup")
    public String googleJoin(@RequestBody Oauth2JoinDto dto) {
        Claims claims = decodeToken(dto.getToken());

        return oidcService.oidcJoin(new JoinUserOIDCDto(claims, dto.getUserType(), SsoType.GOOGLE));
    }

    private Claims decodeToken(String token) {
        return openIdService.decodeToken(new DecodeOpenId(
                token,
                googleAud,
                GOOGLE_ISS,
                SsoType.GOOGLE.name()));
    }
}
