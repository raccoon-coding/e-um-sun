package eumsun.backend.repository.mongoRepository;

import eumsun.backend.domain.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlertRepository extends MongoRepository<Alert, String> {
}
