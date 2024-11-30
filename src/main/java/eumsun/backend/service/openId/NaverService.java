package eumsun.backend.service.openId;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.toService.NaverOauthDto;
import eumsun.backend.dto.request.NaverJoinDto;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static eumsun.backend.config.util.JwtUtil.HEADER;
import static eumsun.backend.config.util.JwtUtil.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NaverService {
    private final String url = "https://openapi.naver.com/v1/nid/me";

    private final UserDataRepositoryValid userDataRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public APIMessage naverJoin(NaverJoinDto dto) {
        NaverOauthDto getResponse = getBody(dto.getAccessToken());
        UserData newUser = UserData.builder()
                .email(getResponse.userEmail())
                .userName(getResponse.userName())
                .password(null)
                .provider(SsoType.NAVER)
                .userType(UserType.valueOf(dto.getUserType()))
                .build();

        userDataRepository.saveNewUserData(newUser);
        return APIServerMessage.회원가입_성공;
    }

    public UserData naverLogin(String accessToken) {
        NaverOauthDto getResponse = getBody(accessToken);

        return userDataRepository.findUserByEmail(getResponse.userEmail());
    }

    private NaverOauthDto getBody(String accessToken) {
        try {
            ResponseEntity<String> response = tryRequestNaver(accessToken);
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode responseNode = rootNode.path("response");
            return new NaverOauthDto(responseNode.path("name").asText(),
                    responseNode.path("email").asText());
        } catch (IOException e) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }
    }

    private ResponseEntity<String> tryRequestNaver(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER, TOKEN_PREFIX + accessToken);
        // HTTP 요청을 보낼 때 사용할 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();
        // HTTP 요청 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);
        // HTTP 요청 보내고 응답 받기
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
}
