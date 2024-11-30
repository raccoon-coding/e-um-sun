package eumsun.backend.controller;

import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.request.controller.UserJoinDto;
import eumsun.backend.dto.request.controller.UserLoginDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.service.UserService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotTokenController {
    private final UserService userService;

    @PostMapping("/join")
    public API<String> join(@Validated @RequestBody UserJoinDto dto) {
        APIMessage apiMessage = userService.joinNewUserFromDefault(dto);
        return new API<>(apiMessage);
    }

    @PostMapping("/login")
    public API<TokenDto> login(@Validated @RequestBody UserLoginDto dto) {
        TokenDto token = userService.login(dto);
        return new API<>(token, APIServerMessage.로그인_성공);
    }

    @GetMapping("/refresh")
    public API<TokenDto> refresh(ServletRequest request) {
        TokenDto token = userService.refreshToken((HttpServletRequest) request);
        return new API<>(token, APIServerMessage.토큰_재발급_성공);
    }
}
