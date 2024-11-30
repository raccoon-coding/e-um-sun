package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Log;
import eumsun.backend.repository.mongoRepository.LogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class LogRepositoryValidTest {
    private final LogRepositoryValid repository;

    @Autowired
    public LogRepositoryValidTest(LogRepository repository) {
        this.repository = new LogRepositoryValid(repository);
    }

    @Test
    void 로그_저장_확인() {
        Log log = Log.builder()
                .requestUrl("/join")
                .requestBody("user name : admin\npassword : 1234")
                .build();

        repository.saveLog(log);
    }
}
