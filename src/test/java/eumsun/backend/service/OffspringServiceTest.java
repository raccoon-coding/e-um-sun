package eumsun.backend.service;

import eumsun.backend.domain.ConnectionState;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.toService.ConnectUserDto;
import eumsun.backend.dto.toService.CreateConnectDto;
import eumsun.backend.exception.service.StringMismatchException;
import eumsun.backend.repository.mongoRepository.ConnectionRepository;
import eumsun.backend.repository.mongoRepository.UserDataRepository;
import eumsun.backend.repository.repositoryImpl.ConnectionRepositoryValid;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
class OffspringServiceTest {
    private final OffspringService offspringService;
    private final UserDataRepositoryValid userDataRepository;
    private final ConnectionRepositoryValid connectionRepository;

    private final static String offspringEmail = "offspring@gmail.com";
    private final static String anotherParentEmail = "parent@naver.com";
    private final static String parentEmail = "parent@gmail.com";

    @Autowired
    public OffspringServiceTest(UserDataRepository userRepository, ConnectionRepository connectRepository) {
        this.userDataRepository = new UserDataRepositoryValid(userRepository);
        this.connectionRepository = new ConnectionRepositoryValid(connectRepository);
        this.offspringService = new OffspringService(this.connectionRepository);
    }

    @BeforeEach
    void 테스트_데이터_설정() {
        ParentService parentService = new ParentService(userDataRepository, connectionRepository);
        userDataRepository.saveNewUserData(offspring());
        userDataRepository.saveNewUserData(parent());
        UserData parent = userDataRepository.findUserByEmail(parentEmail);
        parentService.connectOffspring(new CreateConnectDto(parent, offspringEmail));
    }

    @AfterEach
    void 테스트_데이터_삭제() {
        userDataRepository.deleteByEmail(offspringEmail);
        userDataRepository.deleteByEmail(parentEmail);
    }

    @Test
    void 유저_연결_확인() {
        UserData offspring = userDataRepository.findUserByEmail(offspringEmail);
        UserData parent = userDataRepository.findUserByEmail(parentEmail);
        String randomStr = parent.getConnection().getRandomMixStr();
        ConnectUserDto dto = new ConnectUserDto(offspring, randomStr);

        offspringService.connectParent(dto);

        UserData result = userDataRepository.findUserByEmail(offspringEmail);
        assertThat(result.getConnection().getStatus()).isEqualTo(ConnectionState.ACCEPTED);
    }

    @Test
    void 랜덤_문자열_입력_에러_확인() {
        UserData offspring = userDataRepository.findUserByEmail(offspringEmail);
        String randomStr = "12345";
        ConnectUserDto dto = new ConnectUserDto(offspring, randomStr);

        assertThatThrownBy(() -> {
            offspringService.connectParent(dto);
        }).isInstanceOf(StringMismatchException.class);
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
