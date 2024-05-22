package eumsun.backend.service.openId;

public class OpenIdUtil {
    public static final String CLAIM_NAME = "name";
    public static final String CLAIM_EMAIL = "email";
    public static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";
    public static final String KAKAO_PUBLIC_KEYS_URL = "https://kauth.kakao.com/.well-known/jwks.json";
    public static final String GOOGLE_PUBLIC_KEYS_URL = "https://www.googleapis.com/oauth2/v3/certs";
    public static final String APPLE_ISS = "https://appleid.apple.com";
    public static final String KAKAO_ISS = "https://kauth.kakao.com";
    public static final String GOOGLE_ISS = "https://accounts.google.com";
//    public static final List<String> GOOGLE_ISS = new ArrayList<>(Arrays.asList("https://accounts.google.com",
//            "accounts.google.com"));

    public static final String KEYS = "keys";
}
