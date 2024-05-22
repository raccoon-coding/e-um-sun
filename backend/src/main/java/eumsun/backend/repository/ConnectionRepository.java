package eumsun.backend.repository;

import eumsun.backend.domain.Connection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConnectionRepository extends MongoRepository<Connection, String> {
}
