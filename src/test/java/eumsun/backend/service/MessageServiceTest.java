package eumsun.backend.service;

import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.Message;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.toService.SaveMessageDto;
import eumsun.backend.repository.mongoRepository.ConversationRepository;
import eumsun.backend.repository.mongoRepository.MessageRepository;
import eumsun.backend.repository.mongoRepository.UserDataRepository;
import eumsun.backend.repository.repositoryImpl.ConversationRepositoryValid;
import eumsun.backend.repository.repositoryImpl.MessageRepositoryValid;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class MessageServiceTest {
    private final MessageService messageService;
    private final UserDataRepositoryValid userDataRepository;
    private final ConversationRepositoryValid conversationRepository;
    private final MessageRepositoryValid messageRepository;

    private final static String offspringEmail = "offspring@gmail.com";

    @Autowired
    public MessageServiceTest(MessageRepository messageRepository, ConversationRepository conversationRepository, UserDataRepository userDataRepository) {
        this.userDataRepository = new UserDataRepositoryValid(userDataRepository);
        this.messageRepository = new MessageRepositoryValid(messageRepository);
        this.conversationRepository = new ConversationRepositoryValid(conversationRepository);
        this.messageService = new MessageService(this.messageRepository, this.conversationRepository);
    }

    @BeforeEach
    void 테스트_데이터_설정() {
        userDataRepository.saveNewUserData(offspring());
    }

    @AfterEach
    void 테스트_데이터_삭제() {
        userDataRepository.deleteByEmail(offspringEmail);
    }

    @Test
    void GPT_Assistant_메시지_확인() {
        UserData user = userDataRepository.findUserByEmail(offspringEmail);
        SaveMessageDto dto = new SaveMessageDto(user, "전자레인지에 컵라면을 조리해서 먹어도 될까?", 10);
        messageService.saveAssistantMessage(dto);

        Conversation conversation = user.getConversation();
        List<Message> messages = messageRepository.findMessagesTop10(conversation);
        assertThat(messages.size()).isEqualTo(1);
    }

    @Test
    void GPT_User_메시지_확인() {
        UserData user = userDataRepository.findUserByEmail(offspringEmail);
        SaveMessageDto dto = new SaveMessageDto(user, "전자레인지에 컵라면을 조리해서 먹어도 될까?", 10);
        messageService.saveUserMessage(dto);

        Conversation conversation = user.getConversation();
        List<Message> messages = messageRepository.findMessagesTop10(conversation);
        assertThat(messages.size()).isEqualTo(1);
    }

    UserData offspring() {
        UserData user =  UserData.builder()
                .userName("offSpring")
                .email(offspringEmail)
                .provider(SsoType.DEFAULT)
                .userType(UserType.OFFSPRING)
                .password("1234")
                .build();
        user.createConversation();
        conversationRepository.createConversation(user.getConversation());
        return user;
    }
}
