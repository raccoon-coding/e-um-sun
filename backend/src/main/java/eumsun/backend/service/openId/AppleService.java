package eumsun.backend.service.openId;

import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.parameter.DecodeOpenId;
import eumsun.backend.dto.request.oauth.Oauth2JoinDto;
import eumsun.backend.repository.UserDataRepository;
import eumsun.backend.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static eumsun.backend.service.openId.OpenIdUtil.APPLE_ISS;
import static eumsun.backend.service.openId.OpenIdUtil.CLAIM_EMAIL;
import static eumsun.backend.service.openId.OpenIdUtil.CLAIM_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AppleService {

    private final UserDataRepository userDataRepository;
    private final UserService userService;
    private final OpenIdService openIdService;

    @Value("${openid.apple}")
    private String appleAud;

    @Transactional
    public String appleJoin(Oauth2JoinDto oauth2JoinDto) {

        Claims claims = openIdService.decodeToken(new DecodeOpenId(
                oauth2JoinDto.getToken(),
                appleAud,
                APPLE_ISS,
                SsoType.APPLE.name()));

        UserData newUser = UserData.builder()
                .userName(claims.get(CLAIM_NAME, String.class))
                .email(claims.get(CLAIM_EMAIL, String.class))
                .password(null)
                .userType(UserType.valueOf(oauth2JoinDto.getUserType()))
                .provider(SsoType.APPLE)
                .build();

        userDataRepository.save(newUser);

        return "회원가입이 완료되었습니다.";
    }

    public UserData appleLogin(String appleJson) {

        Claims claims = openIdService.decodeToken(new DecodeOpenId(
                appleJson,
                appleAud,
                APPLE_ISS,
                SsoType.APPLE.name()));

        return userService.findUserDataByEmail(claims.get(CLAIM_EMAIL, String.class));
    }

}
