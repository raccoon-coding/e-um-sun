package eumsun.backend.dto.toService;

import eumsun.backend.domain.UserData;

public record DeleteUserDto(UserData user, String userEmail) {
}
