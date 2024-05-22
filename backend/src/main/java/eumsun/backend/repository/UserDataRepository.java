package eumsun.backend.repository;

import eumsun.backend.domain.UserData;
import eumsun.backend.repository.customInterface.UserDataCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDataRepository extends MongoRepository<UserData, String>,
        UserDataCustomRepository {
}
