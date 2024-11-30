package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.Message;
import eumsun.backend.repository.mongoRepository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConversationRepositoryValid {
    private final ConversationRepository repository;

    public void createConversation(Conversation conversation) {
        repository.save(conversation);
    }

    public void patch(Conversation conversation) {
        repository.save(conversation);
    }

    public void deleteConversation(Conversation conversation) {
        repository.delete(conversation);
    }
}
