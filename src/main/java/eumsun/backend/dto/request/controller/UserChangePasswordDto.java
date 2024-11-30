package eumsun.backend.dto.request.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserChangePasswordDto {
    @NotBlank(message = "이전 비밀번호를 입력해주세요.")
    private final String oldPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private final String newPassword;
}
