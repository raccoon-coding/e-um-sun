package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.Message;
import eumsun.backend.repository.mongoRepository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryValid {
    private final MessageRepository repository;

    public void createMessage(Message message) {
        repository.save(message);
    }

    public List<Message> findMessagesTop10(Conversation conversation) {
        return repository.findTop10ByConversationIdOrderByCreateAtDesc(conversation.getId());
    }
}
