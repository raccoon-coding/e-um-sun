package eumsun.backend.config.jwt.Impl;

import eumsun.backend.config.auth.PrincipalDetailsService;
import eumsun.backend.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

    private final PrincipalDetailsService principalDetailsService;

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = principalDetailsService.loadUserByUsername(getAccessTokenUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
