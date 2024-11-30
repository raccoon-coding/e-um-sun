package eumsun.backend.dto.request.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDeleteDto {

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String userEmail;
}
