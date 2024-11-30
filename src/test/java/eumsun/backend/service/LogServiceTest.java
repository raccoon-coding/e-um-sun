package eumsun.backend.service;

import eumsun.backend.dto.toService.CreateLogDto;
import eumsun.backend.repository.mongoRepository.LogRepository;
import eumsun.backend.repository.repositoryImpl.LogRepositoryValid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class LogServiceTest {
    private final LogService logService;

    @Autowired
    public LogServiceTest(LogRepository repository) {
        this.logService = new LogService(new LogRepositoryValid(repository));
    }

    @Test
    void 로그_생성_확인() {
        CreateLogDto dto = new CreateLogDto(null, "/join", "user name : admin\n password : 1234\n email : admin@gmail.com");
        logService.createLog(dto);
    }
}
