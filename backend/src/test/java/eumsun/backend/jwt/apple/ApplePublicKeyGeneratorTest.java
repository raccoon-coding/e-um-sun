package eumsun.backend.jwt.apple;

import eumsun.backend.config.jwt.openId.OpenIdPublicKeyGenerator;
import eumsun.backend.dto.parameter.OpenIdPublicKey;
import eumsun.backend.dto.parameter.OpenIdPublicKeyDto;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class ApplePublicKeyGeneratorTest {

    private final OpenIdPublicKeyGenerator applePublicKeyGenerator = new OpenIdPublicKeyGenerator();

    @Test
    public void 헤더와_일치하는_키로_퍼블릭_키_생성() {
        // given
        Map<String, Object> headers = new HashMap<>();
        headers.put("kid", "W6WcOKB");
        headers.put("alg", "RS256");

        List<OpenIdPublicKey> keys = new ArrayList<>();
        setKeys(keys);
        OpenIdPublicKeyDto applePublicKeys = new OpenIdPublicKeyDto(keys);

        // when
        PublicKey publicKey = applePublicKeyGenerator.generate(headers, applePublicKeys);

        // then
        assertThat(publicKey.getAlgorithm()).isEqualTo("RSA");
    }

    private void setKeys(List<OpenIdPublicKey> keys) {
        keys.add(new OpenIdPublicKey("W6WcOKB",
                "RSA",
                "RS256",
                "sig",
                "2Zc5d0-zkZ5AKmtYTvxHc3vRc41YfbklflxG9SWsg5qXUxvfgpktGAcxXLFAd9Uglzow9ezvmTGce5d3DhAYKwHAEPT9hbaMDj7DfmEwuNO8UahfnBkBXsCoUaL3QITF5_DAPsZroTqs7tkQQZ7qPkQXCSu2aosgOJmaoKQgwcOdjD0D49ne2B_dkxBcNCcJT9pTSWJ8NfGycjWAQsvC8CGstH8oKwhC5raDcc2IGXMOQC7Qr75d6J5Q24CePHj_JD7zjbwYy9KNH8wyr829eO_G4OEUW50FAN6HKtvjhJIguMl_1BLZ93z2KJyxExiNTZBUBQbbgCNBfzTv7JrxMw",
                "AQAB"
        ));

        keys.add(new OpenIdPublicKey("fh6Bs8C",
                "RSA",
                "RS256",
                "sig",
                "u704gotMSZc6CSSVNCZ1d0S9dZKwO2BVzfdTKYz8wSNm7R_KIufOQf3ru7Pph1FjW6gQ8zgvhnv4IebkGWsZJlodduTC7c0sRb5PZpEyM6PtO8FPHowaracJJsK1f6_rSLstLdWbSDXeSq7vBvDu3Q31RaoV_0YlEzQwPsbCvD45oVy5Vo5oBePUm4cqi6T3cZ-10gr9QJCVwvx7KiQsttp0kUkHM94PlxbG_HAWlEZjvAlxfEDc-_xZQwC6fVjfazs3j1b2DZWsGmBRdx1snO75nM7hpyRRQB4jVejW9TuZDtPtsNadXTr9I5NjxPdIYMORj9XKEh44Z73yfv0gtw",
                "AQAB"
        ));

        keys.add(new OpenIdPublicKey("YuyXoY",
                "RSA",
                "RS256",
                "sig",
                "1JiU4l3YCeT4o0gVmxGTEK1IXR-Ghdg5Bzka12tzmtdCxU00ChH66aV-4HRBjF1t95IsaeHeDFRgmF0lJbTDTqa6_VZo2hc0zTiUAsGLacN6slePvDcR1IMucQGtPP5tGhIbU-HKabsKOFdD4VQ5PCXifjpN9R-1qOR571BxCAl4u1kUUIePAAJcBcqGRFSI_I1j_jbN3gflK_8ZNmgnPrXA0kZXzj1I7ZHgekGbZoxmDrzYm2zmja1MsE5A_JX7itBYnlR41LOtvLRCNtw7K3EFlbfB6hkPL-Swk5XNGbWZdTROmaTNzJhV-lWT0gGm6V1qWAK2qOZoIDa_3Ud0Gw",
                "AQAB"
        ));
    }

}