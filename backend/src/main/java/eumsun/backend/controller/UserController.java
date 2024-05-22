package eumsun.backend.controller;

import eumsun.backend.config.jwt.JwtProvider;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.parameter.CreateTokenDto;
import eumsun.backend.dto.request.JoinDto;
import eumsun.backend.dto.parameter.LoginRequestDto;
import eumsun.backend.dto.request.RefreshTokenDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.response.UserDataDto;
import eumsun.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static eumsun.backend.config.util.JwtUtil.MAX_REFRESH;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/join")
    public String join(@RequestBody JoinDto joinDto) {

        UserData newUser = UserData.builder()
                .userName(joinDto.getUserName())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .email(joinDto.getEmail())
                .provider(SsoType.valueOf(joinDto.getProvider()))
                .userType(UserType.valueOf(joinDto.getUserType()))
                .build(); // security 암호화 작동시키기

        newUser.createConversation(newUser.getId());

        return userService.join(newUser);
    }

    @PostMapping("/login")
    public TokenDto loginDefault(@RequestBody LoginRequestDto loginRequestDto) {

        UserData member = userService.findUserDataByEmail(loginRequestDto.getEmail()); // match email도 해야함.

        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        CreateTokenDto tokenDto = CreateTokenDto.builder()
                .userEmail(member.getEmail())
                .refreshCount(0)
                .build();

        return jwtProvider.createToken(tokenDto);
    }

    @GetMapping("/refresh")
    public TokenDto refreshToken(@RequestHeader RefreshTokenDto refreshTokenDto) {

        String userEmail = jwtProvider.getRefreshUserPk(refreshTokenDto.getAuthorization());
        Integer refreshCount = jwtProvider.getRefreshCount(refreshTokenDto.getRefreshToken());

        if(Objects.equals(refreshCount, MAX_REFRESH)){
            throw new IllegalArgumentException("재 로그인 하십쇼.");
        }

        CreateTokenDto tokenDto = CreateTokenDto.builder()
                .userEmail(userEmail)
                .refreshCount(refreshCount)
                .build();

        return jwtProvider.createToken(tokenDto);
    }

    @GetMapping("/user")
    public UserDataDto user(){

        String userEmail = userService.findUserDataByToken();
        UserData userData = userService.findUserDataByEmail(userEmail);
        String connectedUserEmail = userService.findConnectionEmailByUserData(userData);

        return UserDataDto.builder()
                .id(userData.getId())
                .token(userData.getToken())
                .userName(userData.getUserName())
                .connectedUserEmail(connectedUserEmail)
                .userType(userData.getUserType())
                .email(userData.getEmail())
                .provider(userData.getProvider())
                .build();
    }

    @DeleteMapping("/user/delete")
    public String deleteUser() {

        String userEmail = userService.findUserDataByToken();
        UserData userData = userService.findUserDataByEmail(userEmail);
        if(userData.getConnection() != null){
            userService.deleteConnection(userData.getConnection(), userData.getId());
        }
        if(userData.getConversation() != null){
            userService.deleteConversation(userData.getConversation());
        }

        return userService.deleteUser(userData);
    }
}
