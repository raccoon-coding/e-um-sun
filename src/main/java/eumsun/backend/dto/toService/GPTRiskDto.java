package eumsun.backend.dto.toService;

import eumsun.backend.domain.UserData;
import eumsun.backend.dto.request.service.GPTCompletionRequest;

public record GPTRiskDto(GPTCompletionRequest request, String message, UserData offspring) {
}
