package eumsun.backend.config.util;


public interface JwtUtil {
    String TOKEN_PREFIX = "Bearer "; // 스페이스 필요함
    String HEADER = "Authorization";
    String SUBJECT_ACCESS = "e-um-sun_Access_Token";
    String SUBJECT_REFRESH = "e-um-sun_Refresh_Token";
    String USER_UUID = "id";
    String REFRESH_COUNT = "refreshCount";
    Integer MAX_REFRESH = 10;
    int ONE_HOUR = 3_600_000; // 한 시간를 msec으로 나타낸 값
    int HALF_HOUR = 1_800_000; // 30분을 msec으로 나타낸 값
}
