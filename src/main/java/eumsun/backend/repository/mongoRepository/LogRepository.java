package eumsun.backend.repository.mongoRepository;

import eumsun.backend.domain.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String> {
}
