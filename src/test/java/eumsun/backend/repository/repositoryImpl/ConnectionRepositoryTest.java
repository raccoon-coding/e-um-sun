package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Connection;
import eumsun.backend.domain.ConnectionState;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.repository.mongoRepository.ConnectionRepository;
import eumsun.backend.repository.mongoRepository.UserDataRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ConnectionRepositoryTest {
    private final UserDataRepositoryValid userDataRepository;
    private final ConnectionRepositoryValid connectionRepository;

    private final static String offspringEmail = "offspring@gmail.com";
    private final static String parentEmail = "parent@gmail.com";
    private final static String random = "random";

    @Autowired
    public ConnectionRepositoryTest(UserDataRepository userDataRepository, ConnectionRepository connectionRepository) {
        this.userDataRepository = new UserDataRepositoryValid(userDataRepository);
        this.connectionRepository = new ConnectionRepositoryValid(connectionRepository);
    }

    @BeforeEach
    void 테스트_데이터_생성() {
        userDataRepository.saveNewUserData(offspring());
        userDataRepository.saveNewUserData(parent());
        createConnection();
    }

    @AfterEach
    void 테스트용_데이터_삭제() {
        userDataRepository.deleteByEmail(offspringEmail);
        userDataRepository.deleteByEmail(parentEmail);
    }

    @Test
    void 연결_생성_확인() {
        UserData result = userDataRepository.findUserByEmail(parentEmail);
        assertThat(result.getConnection().getRandomMixStr()).isEqualTo(random);
        assertThat(result.getConnection().getStatus()).isEqualTo(ConnectionState.PENDING);
    }

    @Test
    void 연결_확인_확인() {
        UserData offspring = userDataRepository.findUserByEmail(offspringEmail);
        Connection connection = userDataRepository.findConnectionByUserData(offspring);
        connection.acceptConnect();
        connectionRepository.patch(connection);
        UserData result = userDataRepository.findUserByEmail(offspringEmail);

        assertThat(result.getConnection().getStatus()).isEqualTo(ConnectionState.ACCEPTED);
    }

    @Test
    void 연결_삭제_확인() {
        UserData offspring = userDataRepository.findUserByEmail(offspringEmail);
        Connection connection = userDataRepository.findConnectionByUserData(offspring);
        String parentId = connection.getParent();
        UserData parent = userDataRepository.findUserById(parentId);

        deleteConnection(offspring, parent, connection);

        UserData result = userDataRepository.findUserByEmail(offspringEmail);
        assertThat(result.getConnection()).isNull();
    }

    void createConnection() {
        UserData offspring = userDataRepository.findUserByEmail(offspringEmail);
        UserData parent = userDataRepository.findUserByEmail(parentEmail);
        Connection connection = connection(offspring.getId(), parent.getId());
        parent.connectUser(connection);
        offspring.connectUser(connection);

        connectionRepository.createConnect(connection);
        userDataRepository.patch(parent);
        userDataRepository.patch(offspring);
    }

    void deleteConnection(UserData offspring, UserData parent, Connection connection) {
        connectionRepository.deleteConnect(connection);
        offspring.deleteConnection();
        parent.deleteConnection();
        userDataRepository.patch(offspring);
        userDataRepository.patch(parent);
    }

    Connection connection(String offspringId, String parentId) {
        return Connection.builder()
                .offspringId(offspringId)
                .parentId(parentId)
                .str(random)
                .build();
    }

    UserData offspring() {
        return UserData.builder()
                .userName("offSpring")
                .email(offspringEmail)
                .provider(SsoType.DEFAULT)
                .userType(UserType.OFFSPRING)
                .password("1234")
                .build();
    }

    UserData parent() {
        return UserData.builder()
                .userName("parent")
                .email(parentEmail)
                .provider(SsoType.DEFAULT)
                .userType(UserType.PARENT)
                .password("1234")
                .build();
    }
}
