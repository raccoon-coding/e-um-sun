package eumsun.backend.service;

import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.toService.CreateConnectDto;
import eumsun.backend.dto.toService.GetRandomStringDto;
import eumsun.backend.exception.service.DuplicateConnectionException;
import eumsun.backend.exception.service.NotMatchOffspringUser;
import eumsun.backend.exception.service.ParentNotConnectedException;
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
class ParentServiceTest {
    private final ParentService parentService;
    private final UserDataRepositoryValid userDataRepository;
    private final ConnectionRepositoryValid connectionRepository;

    private final static String offspringEmail = "offspring@gmail.com";
    private final static String anotherParentEmail = "parent@naver.com";
    private final static String parentEmail = "parent@gmail.com";

    @Autowired
    public ParentServiceTest(UserDataRepository userRepository, ConnectionRepository connectRepository) {
        this.userDataRepository = new UserDataRepositoryValid(userRepository);
        this.connectionRepository = new ConnectionRepositoryValid(connectRepository);
        this.parentService = new ParentService(this.userDataRepository, this.connectionRepository);
    }

    @BeforeEach
    void 테스트용_데이터_생성() {
        userDataRepository.saveNewUserData(offspring());
        userDataRepository.saveNewUserData(parent());
    }

    @AfterEach
    void 테스트용_데이터_삭제() {
        userDataRepository.deleteByEmail(offspringEmail);
        userDataRepository.deleteByEmail(parentEmail);
    }

    @Test
    void 연결_확인() {
        UserData parent = userDataRepository.findUserByEmail(parentEmail);

        String randomStr = parentService.connectOffspring(new CreateConnectDto(parent, offspringEmail));
        UserData expect = userDataRepository.findUserByEmail(parentEmail);

        assertThat(randomStr).isEqualTo(expect.getConnection().getRandomMixStr());
    }

    @Test
    void 요청을_두번_할_때() {
        UserData parent = userDataRepository.findUserByEmail(parentEmail);
        parentService.connectOffspring(new CreateConnectDto(parent, offspringEmail));

        assertThatThrownBy(() -> {
            parentService.connectOffspring(new CreateConnectDto(parent, offspringEmail));
        }).isInstanceOf(DuplicateConnectionException.class);
    }

    @Test
    void 요청당한_유저가_보호자가_아닐_때() {
        userDataRepository.saveNewUserData(anotherParent());
        UserData parent = userDataRepository.findUserByEmail(parentEmail);

        assertThatThrownBy(() -> {
            parentService.connectOffspring(new CreateConnectDto(parent, anotherParentEmail));
        }).isInstanceOf(NotMatchOffspringUser.class);
    }

    @Test
    void 랜덤_문자열이_없을_때() {
        UserData parent = userDataRepository.findUserByEmail(parentEmail);

        assertThatThrownBy(() -> {
            parentService.getRandomString(new GetRandomStringDto(parent));
        }).isInstanceOf(ParentNotConnectedException.class);

    }

    @Test
    void 랜덤_문자열_받기() {
        UserData parent = userDataRepository.findUserByEmail(parentEmail);
        String randomStr = parentService.connectOffspring(new CreateConnectDto(parent, offspringEmail));
        UserData result = userDataRepository.findUserByEmail(parentEmail);

        String expect = parentService.getRandomString(new GetRandomStringDto(result));

        assertThat(randomStr).isEqualTo(expect);
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

    UserData anotherParent() {
        return UserData.builder()
                .userName("parent")
                .email(anotherParentEmail)
                .provider(SsoType.DEFAULT)
                .userType(UserType.PARENT)
                .password("1234")
                .build();
    }
}
