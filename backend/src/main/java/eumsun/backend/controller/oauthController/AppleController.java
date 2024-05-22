package eumsun.backend.controller.oauthController;

import eumsun.backend.config.jwt.JwtCreator;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.parameter.CreateTokenDto;
import eumsun.backend.dto.request.oauth.Oauth2JoinDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.service.openId.AppleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/apple")
@RequiredArgsConstructor
public class AppleController {

    private final JwtCreator jwtProvider;
    private final AppleService appleService;

    @PostMapping("/login")
    public TokenDto appleLogin(@RequestBody String appleJson) {

        UserData member = appleService.appleLogin(appleJson);

        CreateTokenDto tokenDto = CreateTokenDto.builder()
                .userEmail(member.getEmail())
                .refreshCount(0)
                .build();

        return jwtProvider.createToken(tokenDto);
    }

    @PostMapping("/signup")
    public String appleJoin(@RequestBody Oauth2JoinDto oauth2JoinDto) {

        return appleService.appleJoin(oauth2JoinDto);
    }
}
