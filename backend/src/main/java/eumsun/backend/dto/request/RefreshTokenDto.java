package eumsun.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder @AllArgsConstructor
public class RefreshTokenDto {
    String authorization;
    String refreshToken;
}
