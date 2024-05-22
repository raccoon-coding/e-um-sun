package eumsun.backend.repository.customInterface;

import eumsun.backend.domain.Message;
import eumsun.backend.domain.UserData;

import java.util.List;

public interface ConversationCustomRepository {
    List<Message> findMessagesByUserId(UserData userData);
}
