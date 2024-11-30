package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Connection;
import eumsun.backend.repository.mongoRepository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConnectionRepositoryValid {
    private final ConnectionRepository repository;

    public void createConnect(Connection connection) {
        repository.save(connection);
    }

    public void patch(Connection connection) {
        repository.save(connection);
    }

    public void deleteConnect(Connection connection) {
        repository.delete(connection);
    }
}
