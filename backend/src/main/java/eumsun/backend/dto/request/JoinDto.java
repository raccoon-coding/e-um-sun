package eumsun.backend.dto.request;

import lombok.Getter;

@Getter
public class JoinDto {
    private String email;
    private String userName;
    private String password;
    private String userType;
    private String provider;
}
