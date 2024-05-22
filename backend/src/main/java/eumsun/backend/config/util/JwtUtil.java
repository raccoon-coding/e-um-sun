package eumsun.backend.config.util;


public class JwtUtil {
    public static final String TOKEN_PREFIX = "Bearer "; // 스페이스 필요함
    public static final String HEADER = "Authorization";
    public static final String SUBJECT_ACCESS = "jwtstudyAccess"; // 서버만 알고 있는 특정한 값
    public static final String SUBJECT_REFRESH = "jwtstudyRefresh"; // 서버만 알고 있는 특정한 값
    public static final String USER_EMAIL = "email";
    public static final String REFRESH_COUNT = "refreshCount";
    public static final Integer MAX_REFRESH = 10;
    public static final String SECRET_KEY = "123akdjfadkqeiojfaaojfioqjpofjqoeijdkajfkljfdao";
    public static final int ONE_DAY = 86_400_000; // 하루를 msec으로 나타낸 값
}
