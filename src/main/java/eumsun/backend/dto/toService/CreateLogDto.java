package eumsun.backend.dto.toService;

public record CreateLogDto(String userId, String url, String body) {
    public static CreateLogDto of(String url, String body) {
        return new CreateLogDto(null, url, body);
    }
}
