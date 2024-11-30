package eumsun.backend.dto.toService;

import eumsun.backend.domain.SsoType;
import io.jsonwebtoken.Claims;

public record JoinUserOIDCDto(Claims claims, String userType, SsoType ssoType) {
}
