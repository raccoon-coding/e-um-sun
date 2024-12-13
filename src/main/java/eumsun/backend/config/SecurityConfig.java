package eumsun.backend.config;

import eumsun.backend.config.jwt.JwtAuthenticationFilter;
import eumsun.backend.config.jwt.JwtProvider;
import eumsun.backend.config.security.SecurityProperties;
import eumsun.backend.domain.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
// secured 어노테이션 활성화, preAuthorize 활성화(default 기능임)
// Security 활성화 -> 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
// 이 클래스는 유저들이 접근하는 것을 막는(권한이 없는 유저들) 코드 설정이다.
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final SecurityProperties securityProperties;

    private final String offspring = UserType.OFFSPRING.name();
    private final String parent = UserType.PARENT.name();
    private final String develop = UserType.DEVELOP.name();

    // 해당 메서드의 리턴되는 객체를 IoC로 등록
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/join", "/login", "/refresh", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/user/**").hasAnyAuthority(offspring, parent, develop)
                        .requestMatchers("/offspring/**").hasAnyAuthority(offspring, develop)
                        .requestMatchers("/parent/**").hasAnyAuthority(parent, develop)
                        .requestMatchers("/develop/**").hasAuthority(develop)
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, securityProperties),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
