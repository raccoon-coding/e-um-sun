package eumsun.backend.repository.mongoRepository;

import eumsun.backend.domain.Connection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public interface ConnectionRepository extends MongoRepository<Connection, String> {
}
