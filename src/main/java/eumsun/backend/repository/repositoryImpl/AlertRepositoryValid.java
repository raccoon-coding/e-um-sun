package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Alert;
import eumsun.backend.repository.mongoRepository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlertRepositoryValid {
    private final AlertRepository repository;

    public void createAlert(Alert alert) {
        repository.save(alert);
    }
}
