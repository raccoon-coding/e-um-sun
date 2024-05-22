package eumsun.backend.service.gpt;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GptTokenCalculate {
    private final EncodingRegistry registry = Encodings.newLazyEncodingRegistry();

    private final Encoding encoding = registry.getEncodingForModel(ModelType.GPT_4);
    private final Integer tokensPerMessage = 3;
    private final Integer tokensPerName = 1;

    public Integer calculateToken(ChatCompletionRequest chatCompletionRequest) {
        AtomicInteger tokenCount = new AtomicInteger();

        chatCompletionRequest.getMessages()
                .forEach(chatMessage -> {
                    tokenCount.addAndGet(tokensPerMessage);
                    tokenCount.addAndGet(encoding.countTokens(chatMessage.getContent()));
                    tokenCount.addAndGet(encoding.countTokens(chatMessage.getRole()));
                    if (chatMessage.getName() != null) {
                        tokenCount.addAndGet(encoding.countTokens(chatMessage.getName()));
                        tokenCount.addAndGet(tokensPerName);
                    }
                });
        return tokenCount.get();
    }
}
