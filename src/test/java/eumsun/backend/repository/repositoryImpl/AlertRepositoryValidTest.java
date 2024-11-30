package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Alert;
import eumsun.backend.repository.mongoRepository.AlertRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class AlertRepositoryValidTest {
    private final AlertRepositoryValid repository;

    @Autowired
    public AlertRepositoryValidTest(AlertRepository repository) {
        this.repository = new AlertRepositoryValid(repository);
    }

    @Test
    void 알람_생성_확인() {
        Alert alert = Alert.builder()
                .content("아이가 컵라면을 전자레인지에 돌리는 위험한 행동을 하고 있습니다.")
                .offspringId("admin")
                .parentId("parent")
                .build();

        repository.createAlert(alert);
    }
}
