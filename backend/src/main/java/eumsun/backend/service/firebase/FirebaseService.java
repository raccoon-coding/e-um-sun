package eumsun.backend.service.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import eumsun.backend.dto.request.FCMDto;
import eumsun.backend.dto.response.FirebaseMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {

    @Value("${firebase.api-url}")
    private String API_URL;
    @Value("${firebase.firebase-json}")
    private String FIREBASE_JSON;

    private final ObjectMapper objectMapper;

    public void sendMessageTo(FCMDto fcmDto) throws IOException {

        String message = makeMessage(fcmDto.getToken(), fcmDto.getTitle(), fcmDto.getBody());

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        log.info(Objects.requireNonNull(response.body()).string());
    }

    private String makeMessage(String token, String title, String body) throws JsonProcessingException {

         FirebaseMessageDto firebaseMessageDto = FirebaseMessageDto.builder()
                .message(FirebaseMessageDto.Message.builder()
                        .token(token)
                        .notification(FirebaseMessageDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(firebaseMessageDto);
    }

    private String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(FIREBASE_JSON).getInputStream())
                .createScoped(List.of("<https://www.googleapis.com/auth/cloud-platform>"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
