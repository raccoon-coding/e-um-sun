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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static eumsun.backend.service.openId.OpenIdUtil.CLAIM_EMAIL;
import static eumsun.backend.service.openId.OpenIdUtil.CLAIM_NAME;
import static eumsun.backend.service.openId.OpenIdUtil.KAKAO_ISS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoService {

    private final UserDataRepository userDataRepository;
    private final UserService userService;
    private final OpenIdService openIdService;

    @Value("${openid.kakao}")
    private String kakaoAud;

    @Transactional
    public String kakaoJoin(Oauth2JoinDto oauth2JoinDto) {

        Claims claims = openIdService.decodeToken(new DecodeOpenId(
                oauth2JoinDto.getToken(),
                kakaoAud,
                KAKAO_ISS,
                SsoType.KAKAO.name()));

        UserData newUser = UserData.builder()
                .email(claims.get(CLAIM_NAME, String.class))
                .userName(claims.get(CLAIM_EMAIL, String.class))
                .password(null)
                .provider(SsoType.KAKAO)
                .userType(UserType.valueOf(oauth2JoinDto.getUserType()))
                .build();

        userDataRepository.save(newUser);

        return "회원가입이 완료되었습니다.";
    }

    public UserData kakaoLogin(String kakaoJson) {

        Claims claims = openIdService.decodeToken(new DecodeOpenId(
                kakaoJson,
                kakaoAud,
                KAKAO_ISS,
                SsoType.KAKAO.name()));
        return userService.findUserDataByEmail(claims.get(CLAIM_NAME, String.class));
    }
}
