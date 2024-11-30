package eumsun.backend.dto.toService;

public record OpenIdPublicKey(String kid,
                              String kty,
                              String alg,
                              String use,
                              String n,
                              String e) {
    public boolean isSameAlg(final String alg) {
        return this.alg.equals(alg);
    }

    public boolean isSameKid(final String kid) {
        return this.kid.equals(kid);
    }
}
