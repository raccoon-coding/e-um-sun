package eumsun.backend.service;

import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.Message;
import eumsun.backend.domain.MessageRole;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.toService.SaveConversationDto;
import eumsun.backend.dto.toService.SaveMessageDto;
import eumsun.backend.repository.repositoryImpl.ConversationRepositoryValid;
import eumsun.backend.repository.repositoryImpl.MessageRepositoryValid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepositoryValid messageRepository;
    private final ConversationRepositoryValid conversationRepository;

    @Transactional
    public void saveAssistantMessage(SaveMessageDto saveMessageDto) {
        UserData user = saveMessageDto.user();
        Conversation userConv = user.getConversation();
        Message message = assisMessage(userConv, saveMessageDto.context(), saveMessageDto.cost());

        saveMessage(new SaveConversationDto(userConv, message));
    }

    @Transactional
    public void saveUserMessage(SaveMessageDto saveMessageDto) {
        UserData user = saveMessageDto.user();
        Conversation userConv = user.getConversation();
        Message message = userMessage(userConv, saveMessageDto.context(), saveMessageDto.cost());

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
        validConversation(userConversation);
        // Conversation add Message
        userConversation.useGPTMessage(saveConversationDto.message());

        // Conversation and Message save MongoDb
        messageRepository.createMessage(saveConversationDto.message());
        conversationRepository.patch(userConversation);
    }

    private void validConversation(Conversation userConversation) {
        if(userConversation == null){
            throw new IllegalArgumentException("연결을 시도할 수 없습니다.");
        }
    }
}
