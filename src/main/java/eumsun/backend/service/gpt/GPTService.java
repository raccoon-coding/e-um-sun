package eumsun.backend.service.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import eumsun.backend.domain.Message;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.dto.request.service.GPTCompletionRequest;
import eumsun.backend.exception.service.ForceQuitEmitter;
import eumsun.backend.repository.repositoryImpl.MessageRepositoryValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static eumsun.backend.config.util.GptUtil.SYSTEM;
import static eumsun.backend.config.util.GptUtil.SYSTEM_PROMPT_OFFSPRING;
import static eumsun.backend.config.util.GptUtil.SYSTEM_PROMPT_PARENT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GPTService {
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;
    private final MessageRepositoryValid messageRepository;

    private final String message = "message";
    private final String content = "content";
    private final String choices = "choices";

    public void callGptStreamApi(ChatCompletionRequest chatCompletionRequest, SseEmitter emitter,
                            StringBuffer stringBuffer) {
        openAiService.streamChatCompletion(chatCompletionRequest)
                .blockingForEach(completion -> {
                    // completion is response form GPT
                    String responseWord = objectMapper.writeValueAsString(completion);
                    sendMessageToOffspring(emitter, responseWord);
                    // transform String
                    JsonNode contentsNode = objectMapper.readTree(responseWord).get(choices);
                    sumGptCharacter(contentsNode, stringBuffer);
                });
    }

    public ChatCompletionResult callGptChatApi(GPTCompletionRequest request, UserData parent) {
        ChatCompletionRequest chatCompletionRequest = GPTCompletionRequest.of(request,
                createSystemPrompt(parent));
        return openAiService.createChatCompletion(chatCompletionRequest);
    }

    public List<ChatMessage> getConversation(UserData user) {
        List<ChatMessage> prompt = createSystemPrompt(user);
        List<Message> messages = messageRepository.findMessagesTop10(user.getConversation());
        if(messages.isEmpty()){
            return prompt;
        }
        messages.forEach(message ->
                prompt.add(new ChatMessage(message.getRole(), message.getContent())));
        return prompt;
    }

    private List<ChatMessage> createSystemPrompt(@NotNull UserData user) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        if(user.getUserType().equals(UserType.OFFSPRING)){
            chatMessageList.add(new ChatMessage(SYSTEM, SYSTEM_PROMPT_OFFSPRING));
        } else {
            chatMessageList.add(new ChatMessage(SYSTEM, SYSTEM_PROMPT_PARENT));
        }
        return chatMessageList;
    }

    private void sendMessageToOffspring(SseEmitter emitter, String responseWord) {
        try{
            emitter.send(SseEmitter.event()
                    .name(message)
                    .data(responseWord, MediaType.APPLICATION_JSON)
            );
        } catch (IOException e) {
            throw new ForceQuitEmitter(APIErrorMessage.유저_강제_종료.getMessage());
        }
    }

    private void sumGptCharacter(@NotNull JsonNode contentNodes, StringBuffer stringBuffer) {
        contentNodes.forEach(contentNode -> {
            JsonNode messageNode = contentNode.get(message);
            if (messageNode != null && messageNode.has(content)) {
                String contentString = messageNode.get(content).asText();
                stringBuffer.append(contentString);
            }
        });
    }
}
