package eumsun.backend.dto.toService;

import java.util.List;
import java.util.Objects;

public record OpenIdPublicKeyDto(List<OpenIdPublicKey> keys) {
    public OpenIdPublicKey getMatchingKey(final String alg, final String kid) {
        return keys.stream()
                .filter(key -> Objects.equals(key.alg(), alg) && Objects.equals(key.kid(), kid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("잘못된 토큰 형태입니다."));
    }
}
