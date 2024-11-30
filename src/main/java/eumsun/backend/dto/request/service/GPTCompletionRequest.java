package eumsun.backend.dto.request.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class GPTCompletionRequest {
    private String message;

    private final String model = "gpt-4-turbo-preview";
    private final String role = "user";
    private final Integer maxToken = 1_000;

    public static ChatCompletionRequest of(GPTCompletionRequest request, List<ChatMessage> systemPrompt) {
        return ChatCompletionRequest.builder()
                .model(request.getModel())
                .messages(convertChatMessage(request, systemPrompt))
                .maxTokens(request.getMaxToken())
                .build();
    }

    private static List<ChatMessage> convertChatMessage(GPTCompletionRequest request, List<ChatMessage> systemPrompt) {
        systemPrompt.add(new ChatMessage(request.getRole(), request.getMessage()));
        return systemPrompt;
    }
}
