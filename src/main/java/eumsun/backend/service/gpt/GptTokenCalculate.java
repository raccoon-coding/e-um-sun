package eumsun.backend.service.gpt;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.stereotype.Service;

@Service
public class GptTokenCalculate {
    private final EncodingRegistry registry = Encodings.newLazyEncodingRegistry();

    private final Encoding encoding = registry.getEncodingForModel(ModelType.GPT_4);
    private final Integer tokensPerMessage = 3;
    private final Integer tokensPerName = 1;

    public Integer calculateToken(ChatCompletionRequest chatCompletionRequest) {
        int tokenCount = 0;
        for (ChatMessage chatMessage : chatCompletionRequest.getMessages()) {
            tokenCount = sumToken(chatMessage, tokenCount);
        }

        return tokenCount;
    }

    private int sumToken(ChatMessage chatMessage, int tokenCount) {
        tokenCount += tokensPerMessage;
        tokenCount += encoding.countTokens(chatMessage.getContent());
        tokenCount += encoding.countTokens(chatMessage.getRole());
        tokenCount += verifyChatName(chatMessage);
        return tokenCount;
    }


    private int verifyChatName(ChatMessage chatMessage) {
        int count = 0;
        if (chatMessage.getName() != null) {
            count += encoding.countTokens(chatMessage.getName());
            count += tokensPerName;
        }
        return count;
    }
}
