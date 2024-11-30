package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Log;
import eumsun.backend.repository.mongoRepository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LogRepositoryValid {
    private final LogRepository repository;

    public void saveLog(Log log) {
        repository.save(log);
    }
}
