package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.Message;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.repository.mongoRepository.ConversationRepository;
import eumsun.backend.repository.mongoRepository.MessageRepository;
import eumsun.backend.repository.mongoRepository.UserDataRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ConversationRepositoryTest {
    private final UserDataRepositoryValid userDataRepository;
    private final ConversationRepositoryValid conversationRepository;
    private final MessageRepositoryValid messageRepository;

    private final static String offspringEmail = "offspring@gmail.com";

    @Autowired
    public ConversationRepositoryTest(UserDataRepository userDataRepository, ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.userDataRepository = new UserDataRepositoryValid(userDataRepository);
        this.conversationRepository = new ConversationRepositoryValid(conversationRepository);
        this.messageRepository = new MessageRepositoryValid(messageRepository);
    }

    @BeforeEach
    void 테스트_데이터_생성() {
        userDataRepository.saveNewUserData(offspring());
        UserData user = userDataRepository.findUserByEmail(offspringEmail);
        user.createConversation();
        conversationRepository.createConversation(user.getConversation());
        createMessage(user);
        conversationRepository.patch(user.getConversation());
        userDataRepository.patch(user);
    }

    @AfterEach
    void 테스트_데이터_삭제() {
        userDataRepository.deleteByEmail(offspringEmail);
    }

    @Test
    void GPT_대화_내역_생성_확인() {
        UserData result = userDataRepository.findUserByEmail(offspringEmail);

        assertThat(result.getConversation()).isNotNull();
    }

    @Test
    void GPT_대화_추가_확인() {
        UserData offspring = userDataRepository.findUserByEmail(offspringEmail);
        Conversation conversation = offspring.getConversation();
        Message message = new Message("user", "컵라면을 전자레인지에 돌려 먹어도 될까?", 10, conversation);
        messageRepository.createMessage(message);
        conversation.useGPTMessage(message);
        conversationRepository.patch(conversation);

        List<Message> messages = messageRepository.findMessagesTop10(conversation);
        assertThat(messages.size()).isEqualTo(2);
    }

    @Test
    void GPT_대화_내역_확인() {
        UserData user = userDataRepository.findUserByEmail(offspringEmail);
        List<Message> messages = messageRepository.findMessagesTop10(user.getConversation());
        assertThat(messages.size()).isEqualTo(1);
    }

    @Test
    void GPT_대화_내역_삭제_확인() {
        UserData user = userDataRepository.findUserByEmail(offspringEmail);
        conversationRepository.deleteConversation(user.getConversation());
        user.deleteConversation();
        userDataRepository.patch(user);

        UserData result = userDataRepository.findUserByEmail(offspringEmail);
        assertThat(result.getConversation()).isNull();
    }

    Conversation createMessage(UserData user) {
        Conversation conversation = user.getConversation();
        Message message = new Message("user", "컵라면을 전자레인지에 돌려 먹어도 될까?", 10, conversation);
        messageRepository.createMessage(message);
        conversation.useGPTMessage(message);
        return conversation;
    }

    UserData offspring() {
        return UserData.builder()
                .userName("offSpring")
                .email(offspringEmail)
                .provider(SsoType.DEFAULT)
                .userType(UserType.OFFSPRING)
                .password("1234")
                .build();
    }
}
