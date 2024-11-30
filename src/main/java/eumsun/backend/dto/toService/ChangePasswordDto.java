package eumsun.backend.dto.toService;

import eumsun.backend.domain.UserData;

public record ChangePasswordDto(UserData user, String oldPassword, String newPassword) {
}
