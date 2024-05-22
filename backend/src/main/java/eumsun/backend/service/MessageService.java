package eumsun.backend.service;

import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.Message;
import eumsun.backend.domain.MessageRole;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.parameter.SaveConversationDto;
import eumsun.backend.dto.parameter.SaveMessageDto;
import eumsun.backend.repository.ConversationRepository;
import eumsun.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final UserService userService;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    @Transactional
    public void saveAssistantMessage(SaveMessageDto saveMessageDto) {
        UserData user = userService.findUserDataByEmail(saveMessageDto.userEmail());

        Conversation userConv = user.getConversation();
        Message message = assisMessage(userConv,
                saveMessageDto.context(),
                saveMessageDto.cost());

        // Conversation에 메시지 추가
        messageRepository.save(message);
        saveMessage(new SaveConversationDto(userConv, message));
    }

    @Transactional
    public void saveUserMessage(SaveMessageDto saveMessageDto) {
        UserData user = userService.findUserDataByEmail(saveMessageDto.userEmail());

        Conversation userConv = user.getConversation();
        Message message = userMessage(userConv,
                saveMessageDto.context(),
                saveMessageDto.cost());

        // Conversation에 메시지 추가
        messageRepository.save(message);
        saveMessage(new SaveConversationDto(userConv, message));
    }

    private Message assisMessage(Conversation conversation ,String context, Integer cost) {
        return Message.builder()
                .conversation(conversation)
                .role(MessageRole.assistant.name())
                .content(context)
                .cost(cost)
                .build();
    }

    private Message userMessage(Conversation conversation ,String context, Integer cost) {
        return Message.builder()
                .conversation(conversation)
                .role(MessageRole.user.name())
                .content(context)
                .cost(cost)
                .build();
    }

    private void saveMessage(SaveConversationDto saveConversationDto) {

        Conversation userConversation = saveConversationDto.conversation();
        if(userConversation == null){
            throw new IllegalArgumentException("연결을 시도할 수 없습니다.");
        }

        // Conversation에 메시지 추가
        userConversation.getMessages().add(saveConversationDto.message());

        // 메시지 저장 전에 영속성 컨텍스트에 Conversation 엔티티를 영속화합니다.
        messageRepository.save(saveConversationDto.message());
        conversationRepository.save(userConversation);
    }
}
