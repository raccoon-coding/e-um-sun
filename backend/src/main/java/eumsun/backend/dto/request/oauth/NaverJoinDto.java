package eumsun.backend.dto.request.oauth;

import lombok.Getter;

@Getter
public class NaverJoinDto {

    private String accessToken;
    private String userType;
}
