package eumsun.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class GPTAsyncConfig {
    @Bean(name = "gpt Risk Judgment Async")
    public Executor gptGptRiskExecutor() {
        ThreadPoolTaskExecutor poolExecutor = new ThreadPoolTaskExecutor();
        poolExecutor.setCorePoolSize(10);
        poolExecutor.setMaxPoolSize(100);
        poolExecutor.setQueueCapacity(100);
        poolExecutor.setThreadNamePrefix("Gpt Risk Judgment -");
        poolExecutor.initialize();

        return poolExecutor;
    }
}
