package eumsun.backend.jwt.google;

import eumsun.backend.service.openId.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoogleTokenValidate {

    @Autowired
    private GoogleService googleService;

//    @Test
//    public void testVerifyToken() throws GeneralSecurityException, IOException {
//        // 실제 토큰을 여기에 넣으면 안 됩니다. 대신 mock된 토큰을 사용하십시오.
//        String mockToken = "MOCK_TOKEN";
//
//        GoogleIdToken.Payload payload = googleService.verifyToken(mockToken);
//
//        // 결과가 false인 것이 정상입니다, 실제 토큰을 사용하지 않았기 때문에.
//
//    }
}
