package eumsun.backend.firebase;

import eumsun.backend.service.firebase.FirebaseService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class fcmTest {

    @MockBean
    FirebaseService firebaseService;

    @Test
    public void fcmSendTest() {

    }
}
