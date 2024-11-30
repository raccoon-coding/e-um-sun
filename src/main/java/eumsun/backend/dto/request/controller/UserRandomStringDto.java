package eumsun.backend.dto.request.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRandomStringDto {
    @NotBlank(message = "코드를 입력해주세요.")
    private String randomString;
}
