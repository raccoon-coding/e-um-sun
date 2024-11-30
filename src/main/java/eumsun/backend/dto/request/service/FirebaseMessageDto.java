package eumsun.backend.dto.request.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class FirebaseMessageDto {
    private boolean    validateOnly;
    private Message    message;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Message {
        private Notification notification;
        private String      token;
        private Data        data;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Notification {
        private String  title;
        private String  body;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Data{
        private String    name;
        private String    description;
    }
}
