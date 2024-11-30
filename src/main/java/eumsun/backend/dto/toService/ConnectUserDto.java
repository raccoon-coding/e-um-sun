package eumsun.backend.dto.toService;

import eumsun.backend.domain.UserData;

public record ConnectUserDto(UserData offspring, String randomStr) {
}
