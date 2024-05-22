package eumsun.backend.config.auth;

import eumsun.backend.domain.UserData;
import eumsun.backend.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


// 시큐리티 설정에서 loginProcessingUrl을 "/login"으로 설정했고, 이 요청이 오면 자동적으로
// UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행된다.
// UserName으로 찾지않고 email로 찾도록 수정 필요하다.
// return null도 리펙필요
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserDataRepository userDataRepository;

    // 시큐리티 session = Authentication = UserDetails가 들어가야함
    // Authentication(UserDetails)로 작동하고
    // 최종적으로 SecuritySession(Authentication(UserDetails))로 작동한다.
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        Optional<UserData> userDataOptional = userDataRepository.findByEmail(userEmail);
        return userDataOptional.map(PrincipalDetails::new).orElse(null);
    }
}
