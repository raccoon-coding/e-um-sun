package eumsun.backend.dto.response;

import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserDataDto {
    private String id;
    private String userName;
    private String email;
    private SsoType provider;
    private UserType userType;
    private String connectedUserEmail;
    private Integer token;
}
