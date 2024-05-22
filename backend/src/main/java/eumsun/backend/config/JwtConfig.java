package eumsun.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JwtConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
