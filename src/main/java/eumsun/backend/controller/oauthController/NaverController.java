package eumsun.backend.controller.oauthController;

import eumsun.backend.config.jwt.JwtCreator;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.api.APIServerMessage;
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

    @PostMapping("/login")
    public API<TokenDto> naverLogin(@RequestBody String naverAccessToken) {
        UserData member = naverService.naverLogin(naverAccessToken);
        CreateTokenDto tokenDto = new CreateTokenDto(member.getId(), 0);
        TokenDto token = jwtProvider.createToken(tokenDto);

        return new API<>(token, APIServerMessage.로그인_성공);
    }

    @PostMapping("/signup")
    public API<String> naverJoin(@RequestBody NaverJoinDto naverJoinDto) {
        APIMessage apiMessage = naverService.naverJoin(naverJoinDto);

        return new API<>(apiMessage);
    }
}
