package eumsun.backend.dto.response;

import eumsun.backend.domain.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserDto {
    private final String userEmail;
    private final String username;
    private final String userType;
    private final String ssoType;

    public GetUserDto(UserData userData) {
        this.userEmail = userData.getEmail();
        this.username = userData.getUserName();
        this.userType = userData.getUserType().toString();
        this.ssoType = userData.getProvider().toString();
    }
}
