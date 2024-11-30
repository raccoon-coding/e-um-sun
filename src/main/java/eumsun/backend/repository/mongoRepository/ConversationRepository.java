package eumsun.backend.repository.mongoRepository;

import eumsun.backend.domain.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
}
