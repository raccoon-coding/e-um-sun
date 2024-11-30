package eumsun.backend.config.security;

import eumsun.backend.domain.UserData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class PrincipalDetails implements UserDetails {
    private final UserData userData;

    // 일반 로그인
    public PrincipalDetails(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }

    public String getUserEmail() {
        return userData.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userType = userData.getUserType().toString();
        return Arrays.stream(userType.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    } // userType으로 권한을 조회하기 위한 코드

    @Override
    public String getPassword() {
        return userData.getPassword();
    }

    @Override
    public String getUsername() {
        return userData.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 이 계정 만료되었는지 확인할꺼니?
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 되어있니?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 변경 기간 설정할꺼니?
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 되어있니?
        // 1년동안 로그인 안하면 휴면계정으로 들어가냐?
    }
}
