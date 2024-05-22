package eumsun.backend.controller.oauthController;

import eumsun.backend.config.jwt.JwtCreator;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.parameter.CreateTokenDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.request.oauth.NaverJoinDto;
import eumsun.backend.service.openId.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/naver")
@RequiredArgsConstructor
public class NaverController {

    private final NaverService naverService;
    private final JwtCreator jwtProvider;

    // 토큰을 헤더에서 받아올까??? 나중에 결정하기.
    @PostMapping("/login")
    public TokenDto naverLogin(@RequestBody String naverAccessToken) {

        UserData member = naverService.naverLogin(naverAccessToken);

        CreateTokenDto tokenDto = CreateTokenDto.builder()
                .userEmail(member.getEmail())
                .refreshCount(0)
                .build();

        return jwtProvider.createToken(tokenDto);
    }

    @PostMapping("/signup")
    public String naverJoin(@RequestBody NaverJoinDto naverJoinDto) {

        return naverService.naverJoin(naverJoinDto);
    }
}
