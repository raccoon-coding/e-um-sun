package eumsun.backend.config.security;

import eumsun.backend.domain.UserData;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.exception.controller.ExpiredToken;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService {
    private final UserDataRepositoryValid repository;

    // 시큐리티 session = Authentication = UserDetails가 들어가야함
    // Authentication(UserDetails)로 작동하고
    // 최종적으로 SecuritySession(Authentication(UserDetails))로 작동한다.
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    public UserDetails loadUserByUserId(String userId, Date iat) throws UsernameNotFoundException {
        UserData userData = repository.findUserById(userId);
        Date updateAt = tranformdate(userData);

        if(iat.before(updateAt)){
            throw new ExpiredToken(APIErrorMessage.유저_변경_후_토큰_만료.getMessage());
        }
        return new PrincipalDetails(userData);
    }

    private Date tranformdate(UserData userData) {
        LocalDateTime update = userData.getUpdateAtPassword();
        ZonedDateTime updateTime = update.atZone(ZoneId.systemDefault());
        return Date.from(updateTime.toInstant());
    }
}
