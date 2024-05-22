package eumsun.backend.repository.impl;

import eumsun.backend.domain.UserData;
import eumsun.backend.repository.UserDataRepository;
import eumsun.backend.repository.customInterface.UserDataCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

@RequiredArgsConstructor
public class UserDataImpl implements UserDataCustomRepository {

    public final MongoTemplate mongoTemplate;

    @Override
    public Optional<UserData> findByEmail(String email) {

        Query query = new Query(Criteria.where("email").is(email));
        return Optional.ofNullable(mongoTemplate.findOne(query, UserData.class));
    }
}
