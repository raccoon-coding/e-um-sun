package eumsun.backend.config.jwt.openId;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eumsun.backend.domain.SsoType;
import eumsun.backend.dto.toService.OpenIdPublicKey;
import eumsun.backend.dto.toService.OpenIdPublicKeyDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static eumsun.backend.service.openId.OpenIdUtil.APPLE_PUBLIC_KEYS_URL;
import static eumsun.backend.service.openId.OpenIdUtil.GOOGLE_PUBLIC_KEYS_URL;
import static eumsun.backend.service.openId.OpenIdUtil.KAKAO_PUBLIC_KEYS_URL;
import static eumsun.backend.service.openId.OpenIdUtil.KEYS;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenIdPublicKeyHolder {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private final Map<SsoType, OpenIdPublicKeyDto> publicKey = new ConcurrentHashMap<>();
    private final Map<SsoType, String> publicKeyUrl = new HashMap<>();

    @PostConstruct
    private void setPublicKeyUrl() {
        publicKeyUrl.put(SsoType.KAKAO, KAKAO_PUBLIC_KEYS_URL);
        publicKeyUrl.put(SsoType.APPLE, APPLE_PUBLIC_KEYS_URL);
        publicKeyUrl.put(SsoType.GOOGLE, GOOGLE_PUBLIC_KEYS_URL);

        initPublicKey(SsoType.APPLE);
        initPublicKey(SsoType.KAKAO);
        initPublicKey(SsoType.GOOGLE);
    }

    public OpenIdPublicKeyDto getPublicKey(SsoType ssoType) {
        return publicKey.get(ssoType);
    }

    public void renewSsoPublicKey(SsoType ssoType) {
        String keysResponse = restTemplate.getForObject(publicKeyUrl.get(ssoType), String.class);
        publicKey.replace(ssoType, getOpenIdPublicKey(keysResponse));
    }

    private void initPublicKey(SsoType ssoType) {
        String keysResponse = restTemplate.getForObject(publicKeyUrl.get(ssoType), String.class);
        publicKey.put(ssoType, getOpenIdPublicKey(keysResponse));
    }

    private OpenIdPublicKeyDto getOpenIdPublicKey(String keysResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(keysResponse);
            JsonNode keysNode = rootNode.path(KEYS);

            List<OpenIdPublicKey> keys = new ArrayList<>();
            keysNode.forEach(node -> {
                keys.add(parseKeyNode(node));
            });
            return new OpenIdPublicKeyDto(keys);
        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }
    }

    private OpenIdPublicKey parseKeyNode(JsonNode keyNode) {
        return new OpenIdPublicKey(
                keyNode.path("kid").asText(),
                keyNode.path("kty").asText(),
                keyNode.path("alg").asText(),
                keyNode.path("use").asText(),
                keyNode.path("n").asText(),
                keyNode.path("e").asText()
        );
    }
}
