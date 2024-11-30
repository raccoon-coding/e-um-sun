package eumsun.backend.dto.toService;

import eumsun.backend.domain.UserData;

public record SaveMessageDto(UserData user, String context, Integer cost) {
}
