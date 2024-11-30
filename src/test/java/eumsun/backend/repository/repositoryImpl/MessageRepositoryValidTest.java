package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Message;
import eumsun.backend.repository.mongoRepository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class MessageRepositoryValidTest {
    private final MessageRepositoryValid repository;

    @Autowired
    public MessageRepositoryValidTest(MessageRepository repository) {
        this.repository = new MessageRepositoryValid(repository);
    }

    @Test
    void GPT_메시지_저장_확인() {
        Message message = Message.builder()
                .content("컵라면을 전자레인지에 돌려도 될까?")
                .role("user")
                .cost(10)
                .build();

        repository.createMessage(message);
    }
}
