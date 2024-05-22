package eumsun.backend.service.gpt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import eumsun.backend.domain.Message;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.parameter.SaveMessageDto;
import eumsun.backend.dto.request.GPTCompletionRequest;
import eumsun.backend.repository.ConversationRepository;
import eumsun.backend.service.MessageService;
import eumsun.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static eumsun.backend.config.util.GptUtil.SYSTEM;
import static eumsun.backend.config.util.GptUtil.SYSTEM_PROMPT_OFFSPRING;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GPTService {

    private final ConversationRepository conversationRepository;

    private final UserService userService;
    private final GptTokenCalculate gptTokenCalculate;
    private final MessageService messageService;

    public List<ChatMessage> createSystemPrompt () {

        List<ChatMessage> chatMessageList = new ArrayList<>();
        chatMessageList.add(new ChatMessage(SYSTEM, SYSTEM_PROMPT_OFFSPRING));
        return getConversation(chatMessageList);
    }

    @Transactional
    public void saveResponseMessage(StringBuffer stringBuffer, GPTCompletionRequest completionRequest) {

        String userEmail = userService.findUserDataByToken();
        messageService.saveAssistantMessage(new SaveMessageDto(userEmail,
                stringBuffer.toString(),
                0));
    }

    @Transactional
    public void saveRequestMessage(String question, ChatCompletionRequest chatCompletionRequest) {

        String userEmail = userService.findUserDataByToken();
        messageService.saveUserMessage(new SaveMessageDto(userEmail,
                question,
                getResponseToken(chatCompletionRequest)));
    }

    public Integer getResponseToken(ChatCompletionRequest chatCompletionRequest) {

        return gptTokenCalculate.calculateToken(chatCompletionRequest);
    }

    private List<ChatMessage> getConversation(List<ChatMessage> chatMessageList) {

        String userEmail = userService.findUserDataByToken();
        UserData user = userService.findUserDataByEmail(userEmail);
        List<Message> messages = conversationRepository.findMessagesByUserId(user);
        if(messages.isEmpty()){
            return chatMessageList;
        }

        messages.forEach(message ->
                chatMessageList.add(new ChatMessage(message.getRole().toString(), message.getContent()))
        );
        return chatMessageList;
    }
}
