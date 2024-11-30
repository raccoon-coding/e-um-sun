package eumsun.backend.repository.mongoRepository;

import eumsun.backend.domain.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

@EnableMongoRepositories
public interface UserDataRepository extends MongoRepository<UserData, String> {
    Optional<UserData> findByEmail(String email);
    void deleteByEmail(String email);
}
