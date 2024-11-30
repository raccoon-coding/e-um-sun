package eumsun.backend.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends DateTime {
    @Id
    private String id;

    private String userId;
    private String requestUrl;
    private String requestBody;

    @Builder
    public Log(String userId, String requestUrl, String requestBody) {
        this.userId = userId;
        this.requestUrl = requestUrl;
        this.requestBody = requestBody;
    }
}
