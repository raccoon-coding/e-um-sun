package eumsun.backend.controller.oauthController;

import eumsun.backend.config.jwt.JwtCreator;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.parameter.CreateTokenDto;
import eumsun.backend.dto.request.oauth.Oauth2JoinDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.service.openId.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/oauth/google")
@RequiredArgsConstructor
public class GoogleController {

    private final JwtCreator jwtProvider;
    private final GoogleService googleService;

    @PostMapping("/login")
    public TokenDto googleLogin(@RequestBody String googleJson) throws GeneralSecurityException, IOException {

        UserData member = googleService.googleLogin(googleJson);

        CreateTokenDto tokenDto = CreateTokenDto.builder()
                .userEmail(member.getEmail())
                .refreshCount(0)
                .build();

        return jwtProvider.createToken(tokenDto);
    }

    @PostMapping("/signup")
    public String googleJoin(@RequestBody Oauth2JoinDto oauth2JoinDto) throws GeneralSecurityException, IOException {

        return googleService.googleJoin(oauth2JoinDto);
    }
}
