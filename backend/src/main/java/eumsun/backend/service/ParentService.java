package eumsun.backend.service;

import eumsun.backend.domain.Connection;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.repository.ConnectionRepository;
import eumsun.backend.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentService {

    private final UserDataRepository userDataRepository;
    private final UserService userService;
    private final ConnectionRepository connectionRepository;

    @Transactional
    public String connectOffspring(String offspringEmail) {

        String userEmail = userService.findUserDataByToken();
        UserData parent = userService.findUserDataByEmail(userEmail);
        UserData offspring = userService.findUserDataByEmail(offspringEmail);

        if (!offspring.getUserType().equals(UserType.OFFSPRING)) {
            return "해당하는 유저는 자식 유저가 아닙니다.";
        }

        if (parent.getConnection() != null || offspring.getConnection() != null) {
            return "이미 연결되었습니다.";
        }

        Connection connection = Connection.builder()
                .offspringId(offspring.getId())
                .parentId(parent.getId())
                .str(generateRandomMixStr())
                .build();
        // 부모와 자식 모두에게 Connection 설정
        parent.connectUser(connection);
        offspring.connectUser(connection);

        connectionRepository.save(connection);
        userDataRepository.save(parent);
        userDataRepository.save(offspring);

        return "해당하는 유저에게 요청을 보냈습니다.\n" + "연결시 필요한 문자열은 다음과 같습니다. : " + connection.getRandomMixStr();
    }

    public String getString() {

        String userEmail = userService.findUserDataByToken();
        UserData parent = userService.findUserDataByEmail(userEmail);
        Connection parentConnection = parent.getConnection();

        if(parentConnection == null){
            return "연결을 시도할 수 없습니다.";
        }

        return parentConnection.getRandomMixStr();
    }

    private String generateRandomMixStr() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
