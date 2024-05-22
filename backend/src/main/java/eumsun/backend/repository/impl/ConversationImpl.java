package eumsun.backend.repository.impl;

import eumsun.backend.domain.Message;
import eumsun.backend.domain.UserData;
import eumsun.backend.repository.customInterface.ConversationCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableMongoRepositories
@RequiredArgsConstructor
public class ConversationImpl implements ConversationCustomRepository {

    public final MongoTemplate mongoTemplate;

    @Override
    public List<Message> findMessagesByUserId(UserData userData){
        Query query = new Query(Criteria.where("userId").is(userData.getId()));
        return mongoTemplate.find(query, Message.class);
    }
}
