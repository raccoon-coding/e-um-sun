package eumsun.backend.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있는데, /login으로 요청해서 userName과 password를 보내면 동작

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtProvider.getToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인합니다.

        if (token != null && jwtProvider.validateAccessToken(token)) {

            String userEmail = jwtProvider.getAccessTokenUserPk(token);
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("토큰 유효하다");
        }
        else {
            log.info("토큰이 유효하지 않습니다.");
        }
        chain.doFilter(request, response);
    }
}