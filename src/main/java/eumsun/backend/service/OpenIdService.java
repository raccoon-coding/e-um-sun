package eumsun.backend.service;

import eumsun.backend.config.jwt.openId.OpenIdPublicKeyGenerator;
import eumsun.backend.config.jwt.openId.OpenIdPublicKeyHolder;
import eumsun.backend.config.jwt.openId.OpenIdTokenParser;
import eumsun.backend.domain.SsoType;
import eumsun.backend.dto.toService.DecodeOpenId;
import eumsun.backend.dto.toService.OpenIdPublicKeyDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenIdService {
    private final OpenIdTokenParser openIdTokenParser;
    private final OpenIdPublicKeyGenerator openIdPublicKeyGenerator;
    private final OpenIdPublicKeyHolder publicKeyHolder;

    public Claims decodeToken(DecodeOpenId decodeOpenId) {
        final String decodeToken = openIdTokenParser.getDecodeToken(decodeOpenId.token());
        openIdTokenParser.verifyOpenIdToken(decodeOpenId.aud(), decodeOpenId.iss(), decodeToken); // 토큰 1차 검증

        final PublicKey publicKey = getPublicKey(decodeToken, decodeOpenId.ssoType());
        return openIdTokenParser.extractClaims(decodeToken, publicKey); // 토큰 2차 검증
    }

    private PublicKey getPublicKey(String decodeToken, String ssoType) {
        try {
            return matchingKeys(decodeToken, ssoType);
        }
        catch (RuntimeException e){
            publicKeyHolder.renewSsoPublicKey(SsoType.valueOf(ssoType));
            return matchingKeys(decodeToken, ssoType);
        }
    }

    private PublicKey matchingKeys(String decodeToken, String ssoType) {
        final OpenIdPublicKeyDto publicKeys = publicKeyHolder.getPublicKey(SsoType.valueOf(ssoType));
        return openIdPublicKeyGenerator.
                generate(openIdTokenParser.getMapPayloads(decodeToken), publicKeys);
    }
}
