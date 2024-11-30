package eumsun.backend.config.jwt.openId;

import eumsun.backend.dto.toService.OpenIdPublicKey;
import eumsun.backend.dto.toService.OpenIdPublicKeyDto;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class OpenIdPublicKeyGenerator {
    private static final String SIGN_ALGORITHM_HEADER = "alg";
    private static final String KEY_ID_HEADER = "kid";
    private static final int POSITIVE_SIGN_NUMBER = 1;

    public PublicKey generate(final Map<String, Object> payloads, final OpenIdPublicKeyDto publicKeys) {
        try{
            final OpenIdPublicKey publicKey = publicKeys.getMatchingKey(
                    payloads.get(SIGN_ALGORITHM_HEADER).toString(),
                    payloads.get(KEY_ID_HEADER).toString());
            return generatePublicKey(publicKey);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    private PublicKey generatePublicKey(final OpenIdPublicKey openIdPublicKey) {
        try {
            final RSAPublicKeySpec rsaPublicKeySpec = generateRSAKey(openIdPublicKey);
            final KeyFactory keyFactory = KeyFactory.getInstance(openIdPublicKey.kty());
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException("잘못된 OpenId 공개 키");
        }
    }

    private RSAPublicKeySpec generateRSAKey(final OpenIdPublicKey openIdPublicKey) {
        final byte[] nBytes = Base64.getUrlDecoder().decode(openIdPublicKey.n());
        final byte[] eBytes = Base64.getUrlDecoder().decode(openIdPublicKey.e());

        final BigInteger n = new BigInteger(POSITIVE_SIGN_NUMBER, nBytes);
        final BigInteger e = new BigInteger(POSITIVE_SIGN_NUMBER, eBytes);
        return new RSAPublicKeySpec(n, e);
    }
}
