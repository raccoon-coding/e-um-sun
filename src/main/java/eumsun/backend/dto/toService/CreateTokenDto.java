package eumsun.backend.dto.toService;

public record CreateTokenDto(String userId, Integer refreshCount) {
}
