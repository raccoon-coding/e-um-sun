package eumsun.backend.service.openId;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.request.oauth.NaverJoinDto;
import eumsun.backend.dto.parameter.OauthDto;
import eumsun.backend.repository.UserDataRepository;
import eumsun.backend.service.UserService;
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

    private final UserDataRepository userDataRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Transactional
    public String naverJoin(NaverJoinDto naverJoinDto) {

        OauthDto getResponse = getBody(naverJoinDto.getAccessToken());

        UserData newUser = UserData.builder()
                .email(getResponse.userEmail())
                .userName(getResponse.userName())
                .password(null)
                .provider(SsoType.NAVER)
                .userType(UserType.valueOf(naverJoinDto.getUserType()))
                .build();

        userDataRepository.save(newUser);

        return "회원가입이 완료되었습니다.";
    }

    public UserData naverLogin(String accessToken) {

        OauthDto getResponse = getBody(accessToken);

        return userService.findUserDataByEmail(getResponse.userEmail());
    }

    private OauthDto getBody(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER, TOKEN_PREFIX + accessToken);

        // HTTP 요청을 보낼 때 사용할 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // HTTP 요청 보내고 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode responseNode = rootNode.path("response");
            return new OauthDto(responseNode.path("name").asText(),
                    responseNode.path("email").asText());
        } catch (IOException e) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }
    }
}
