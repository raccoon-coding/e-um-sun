package eumsun.backend.dto.parameter;

public record DecodeOpenId(String token,
                           String aud,
                           String iss,
                           String ssoType) {
}
