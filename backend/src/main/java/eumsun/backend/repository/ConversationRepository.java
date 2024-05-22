package eumsun.backend.repository;

import eumsun.backend.domain.Conversation;
import eumsun.backend.repository.customInterface.ConversationCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<Conversation, String>,
        ConversationCustomRepository {
}
