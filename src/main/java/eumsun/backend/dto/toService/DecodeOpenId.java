package eumsun.backend.dto.toService;

public record DecodeOpenId(String token,
                           String aud,
                           String iss,
                           String ssoType) {
}
