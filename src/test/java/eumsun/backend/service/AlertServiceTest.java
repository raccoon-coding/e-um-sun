package eumsun.backend.service;

import eumsun.backend.dto.toService.CreateAlertDto;
import eumsun.backend.repository.mongoRepository.AlertRepository;
import eumsun.backend.repository.repositoryImpl.AlertRepositoryValid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class AlertServiceTest {
    private final AlertService alertService;

    @Autowired
    public AlertServiceTest(AlertRepository repository) {
        this.alertService = new AlertService(new AlertRepositoryValid(repository));
    }

    @Test
    void 알람_생성_확인() {
        CreateAlertDto dto = new CreateAlertDto("userId", "parentId", "유저가 전자레인지에 컵라면을 조리해서 먹을려고 합니다. 빨리 가서 조치해주세요.");
        alertService.createAlert(dto);
    }
}
