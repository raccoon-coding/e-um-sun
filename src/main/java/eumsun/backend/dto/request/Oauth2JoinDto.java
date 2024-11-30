package eumsun.backend.dto.request;

import lombok.Getter;

@Getter
public class Oauth2JoinDto {

    private String token;
    private String userType;
}
