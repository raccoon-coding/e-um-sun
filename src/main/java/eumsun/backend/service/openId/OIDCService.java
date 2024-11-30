package eumsun.backend.service.openId;

import eumsun.backend.config.jwt.JwtCreator;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.toService.CreateTokenDto;
import eumsun.backend.dto.toService.JoinUserOIDCDto;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static eumsun.backend.service.openId.OpenIdUtil.CLAIM_EMAIL;
import static eumsun.backend.service.openId.OpenIdUtil.CLAIM_NAME;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OIDCService {
    private final UserDataRepositoryValid userDataRepository;
    private final JwtCreator jwtProvider;

    @Transactional
    public APIMessage oidcJoin(JoinUserOIDCDto dto) {
        Claims claims = dto.claims();
        UserData newUser = UserData.builder()
                .email(claims.get(CLAIM_EMAIL, String.class))
                .userName(claims.get(CLAIM_NAME, String.class))
                .password(null)
                .provider(dto.ssoType())
                .userType(UserType.valueOf(dto.userType()))
                .build();

        userDataRepository.saveNewUserData(newUser);
        return APIServerMessage.회원가입_성공;
    }

    public TokenDto oidcLogin(Claims claims) {
        UserData user = userDataRepository.findUserByEmail(claims.get(CLAIM_EMAIL, String.class));
        CreateTokenDto tokenDto = new CreateTokenDto(user.getId(), 0);

        return jwtProvider.createToken(tokenDto);
    }
}
