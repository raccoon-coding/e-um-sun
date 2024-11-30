package eumsun.backend.repository.mongoRepository;

import eumsun.backend.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findTop10ByConversationIdOrderByCreateAtDesc(String conversationId);
}
