package eumsun.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JwtConfig {
    @Value("${spring.jwt.secretKey")
    public String SECRET_KEY;
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
