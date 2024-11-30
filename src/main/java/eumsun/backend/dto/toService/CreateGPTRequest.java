package eumsun.backend.dto.toService;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import eumsun.backend.dto.request.service.GPTCompletionRequest;

public record CreateGPTRequest(GPTCompletionRequest completionRequest, StringBuffer stringBuffer,
                               ChatCompletionRequest chatCompletionRequest) {
}
