package eumsun.backend.dto.parameter;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}