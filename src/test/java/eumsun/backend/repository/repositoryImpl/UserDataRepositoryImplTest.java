package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Connection;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.exception.repository.NotExistConnection;
import eumsun.backend.exception.repository.NotExistUserData;
import eumsun.backend.repository.mongoRepository.UserDataRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
class UserDataRepositoryImplTest {
    private final UserDataRepositoryValid repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final String offspringEmail = "offspring@google.com";
    private final String offspringName = "offspring";
    private final String offspringPassword = "offspringPassword";

    public UserDataRepositoryImplTest(@Autowired UserDataRepository repository) {
        this.repository = new UserDataRepositoryValid(repository);
    }

    @BeforeEach
    void createNewOffspringUser() {
        createNewOffspring();
    }

    @AfterEach
    void deleteNewOffspringUser() {
        repository.deleteByEmail(offspringEmail);
    }

    @Test
    void 새로운유저_저장_및_확인() {
        UserData expect = UserData.builder()
                .userName("raccoon")
                .provider(SsoType.DEFAULT)
                .email("raccoon@gmail.com")
                .password(bCryptPasswordEncoder.encode("raccoon"))
                .userType(UserType.DEVELOP)
                .build();

        repository.saveNewUserData(expect);
        UserData result = repository.findUserByEmail("raccoon@gmail.com");

        assertThat(result).isEqualTo(expect);
    }

    @Test
    void 저장된_유저_이메일로_찾기_확인() {
        UserData result = repository.findUserByEmail(offspringEmail);

        assertThat(result.getEmail()).isEqualTo(offspringEmail);
    }

    @Test
    void 저장된_유저_삭제_확인() {
        repository.deleteByEmail(offspringEmail);

        assertThatThrownBy(() -> {
            repository.findUserByEmail(offspringEmail);
        }).isInstanceOf(NotExistUserData.class);

    }

    @Test
    void 유저_데이터_수정_확인() {
        UserData offspring = repository.findUserByEmail(offspringEmail);
        String expect = bCryptPasswordEncoder.encode("newPassword");
        repository.changeUserPassword(offspring, expect);

        UserData result = repository.findUserByEmail(offspringEmail);
        assertThat(result.getPassword()).isEqualTo(expect);
    }

    @Test
    void 유저_연결_확인() {
        UserData offspring = repository.findUserByEmail(offspringEmail);
        assertThatThrownBy(() -> {
            repository.findConnectionByUserData(offspring);
        }).isInstanceOf(NotExistConnection.class);
    }

    @Transactional
    private void createNewOffspring() {
        UserData offspring = UserData.builder()
                .userName(offspringName)
                .email(offspringEmail)
                .password(bCryptPasswordEncoder.encode(offspringPassword))
                .userType(UserType.OFFSPRING)
                .provider(SsoType.DEFAULT).build();

        repository.saveNewUserData(offspring);
    }
}
