package eumsun.backend.config.jwt.Impl;

import eumsun.backend.config.security.PrincipalDetailsService;
import eumsun.backend.config.jwt.JwtProvider;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {
    private final PrincipalDetailsService principalDetailsService;

    @Value("${spring.jwt.secretKey}")
    private String secretKey;
    private SecretKey key;

    @PostConstruct
    private void initializeKey(String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    @Override
    public Authentication getAuthentication(String token, Date iat) {
        UserDetails userDetails = principalDetailsService.loadUserByUserId(getAccessTokenUserPk(token), iat);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public SecretKey getKey() {
        return this.key;
    }
}
