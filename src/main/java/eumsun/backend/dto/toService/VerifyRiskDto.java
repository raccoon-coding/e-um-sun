package eumsun.backend.dto.toService;

import com.theokanning.openai.completion.chat.ChatCompletionResult;
import eumsun.backend.domain.UserData;

public record VerifyRiskDto(UserData offspring, UserData parent, ChatCompletionResult gptResult, String message) {
    public static VerifyRiskDto from(GPTRiskDto dto, UserData parent, ChatCompletionResult result) {
        return new VerifyRiskDto(dto.offspring(), parent, result, dto.message());
    }
}
