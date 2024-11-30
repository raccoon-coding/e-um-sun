package eumsun.backend.controller.oauthController;

import eumsun.backend.config.jwt.JwtCreator;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.request.NaverJoinDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.toService.CreateTokenDto;
import eumsun.backend.service.openId.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/naver")
public class NaverController {
    private final NaverService naverService;
    private final JwtCreator jwtProvider;

    // 토큰을 헤더에서 받아올까??? 나중에 결정하기.
    @PostMapping("/login")
    public TokenDto naverLogin(@RequestBody String naverAccessToken) {
        UserData member = naverService.naverLogin(naverAccessToken);
        CreateTokenDto tokenDto = new CreateTokenDto(member.getId(), 0);

        return jwtProvider.createToken(tokenDto);
    }

    @PostMapping("/signup")
    public String naverJoin(@RequestBody NaverJoinDto naverJoinDto) {
        return naverService.naverJoin(naverJoinDto);
    }
}
