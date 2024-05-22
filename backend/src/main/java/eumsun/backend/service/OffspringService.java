package eumsun.backend.service;

import eumsun.backend.domain.Connection;
import eumsun.backend.domain.UserData;
import eumsun.backend.repository.ConnectionRepository;
import eumsun.backend.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OffspringService {

    private final UserDataRepository userDataRepository;
    private final UserService userService;
    private final ConnectionRepository connectionRepository;

    @Transactional
    public String connectParent(String str) {

        String userEmail = userService.findUserDataByToken();
        UserData offspring = userService.findUserDataByEmail(userEmail);

        Connection connection = offspring.getConnection();
        UserData parent = userService.validUserData(connection.getParent());

        if(Objects.equals(connection.getRandomMixStr(), str)){
            return connecting(offspring, parent, connection);
        }
        else {
            return "문자열이 일치하지 않습니다.";
        }
    }

    private String connecting(UserData offspring, UserData parent, Connection connection) {
        connection.acceptConnect();

        offspring.connectUser(connection);
        parent.connectUser(connection);

        connectionRepository.save(connection);
        userDataRepository.save(offspring);
        userDataRepository.save(parent);

        return "보호자와 연동되었습니다.";
    }
}
