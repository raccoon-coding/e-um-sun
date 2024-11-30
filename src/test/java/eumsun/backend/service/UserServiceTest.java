package eumsun.backend.service;

import eumsun.backend.config.security.PrincipalDetailsService;
import eumsun.backend.config.jwt.Impl.JwtProviderImpl;
import eumsun.backend.config.jwt.JwtProvider;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.request.controller.UserJoinDto;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.request.controller.UserLoginDto;
import eumsun.backend.dto.response.GetUserDto;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.toService.ChangePasswordDto;
import eumsun.backend.dto.toService.DeleteUserDto;
import eumsun.backend.dto.toService.GetUserDataDto;
import eumsun.backend.exception.controller.InvalidRefreshTokenException;
import eumsun.backend.exception.controller.NotEqualOldPassword;
import eumsun.backend.exception.controller.NotEqualUserPasswordException;
import eumsun.backend.exception.repository.ExistUserData;
import eumsun.backend.exception.repository.NotExistUserData;
import eumsun.backend.repository.mongoRepository.ConnectionRepository;
import eumsun.backend.repository.mongoRepository.ConversationRepository;
import eumsun.backend.repository.mongoRepository.UserDataRepository;
import eumsun.backend.repository.repositoryImpl.ConnectionRepositoryValid;
import eumsun.backend.repository.repositoryImpl.ConversationRepositoryValid;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
class UserServiceTest {
    private UserService userService;
    private BCryptPasswordEncoder encoder;
    private UserDataRepositoryValid userDataRepository;

    private final String userEmail = "admin@gmail.com";
    private final String userPassword = "1234qwer";

    @Autowired
    UserServiceTest(UserDataRepository repository, ConnectionRepository connectionRepository, ConversationRepository conversationRepository) {
        this.userDataRepository = new UserDataRepositoryValid(repository);
        this.encoder = new BCryptPasswordEncoder();
        JwtProvider jwtProvider = new JwtProviderImpl(new PrincipalDetailsService(this.userDataRepository));
        this.userService = new UserService(jwtProvider,
                this.encoder,
                this.userDataRepository,
                new ConnectionRepositoryValid(connectionRepository),
                new ConversationRepositoryValid(conversationRepository));
    }

    @AfterEach
    void 테스트_데이터_삭제() {
        try{
            UserData user = userDataRepository.findUserByEmail(userEmail);
            DeleteUserDto dto = new DeleteUserDto(user, userEmail);
            userService.deleteUser(dto);
        } catch (RuntimeException ignored) {
            ;
        }
    }

    @Test
    void 신규가입_Offspring_확인() {
        UserJoinDto dto = createNewUser();

        APIMessage result = userService.joinNewUserFromDefault(dto);

        assertThat(result.getMessage()).isEqualTo(APIServerMessage.회원가입_성공.getMessage());
    }

    @Test
    void 중복가입_Offspring_실패_확인() {
        UserJoinDto first = createNewUser();
        UserJoinDto second = createNewUser();
        userService.joinNewUserFromDefault(first);

        assertThatThrownBy(() -> {
            userService.joinNewUserFromDefault(second);
        }).isInstanceOf(ExistUserData.class);
    }

    @Test
    void 신규가입_Parent_확인() {
        UserJoinDto dto = createNewUser();

        APIMessage result = userService.joinNewUserFromDefault(dto);

        assertThat(result.getMessage()).isEqualTo(APIServerMessage.회원가입_성공.getMessage());
    }

    @Test
    void 로그인_확인() {
        UserJoinDto dto = createNewUser();
        UserLoginDto loginDto = new UserLoginDto(userEmail, userPassword);

        userService.joinNewUserFromDefault(dto);
        TokenDto result = userService.login(loginDto);

        assertThat(result).isNotNull();
    }

    @Test
    void 로그인_실패() {
        UserJoinDto dto = createNewUser();
        UserLoginDto loginDto = new UserLoginDto(userEmail, "password");

        userService.joinNewUserFromDefault(dto);
        assertThatThrownBy(() -> {
            userService.login(loginDto);
        }).isInstanceOf(NotEqualUserPasswordException.class);
    }

    @Test
    void 비밀번호_변경_확인() {
        String newPassword = "newPassword";
        UserJoinDto joinDto = createNewUser();
        userService.joinNewUserFromDefault(joinDto);
        UserData user = userDataRepository.findUserByEmail(userEmail);
        ChangePasswordDto passwordDto = new ChangePasswordDto(user, userPassword, newPassword);

        userService.changeUserPassword(passwordDto);
        UserData result = userDataRepository.findUserByEmail(userEmail);

        assertThat(result.getEmail()).isEqualTo(userEmail);
        assertThat(encoder.matches(newPassword, result.getPassword())).isTrue();
    }

    @Test
    void 비밀번호_변경_실패_확인() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        UserJoinDto joinDto = createNewUser();

        userService.joinNewUserFromDefault(joinDto);
        UserData user = userDataRepository.findUserByEmail(userEmail);
        ChangePasswordDto passwordDto = new ChangePasswordDto(user, oldPassword, newPassword);

        assertThatThrownBy(() -> {
            userService.changeUserPassword(passwordDto);
        }).isInstanceOf(NotEqualOldPassword.class);
    }

    @Test
    void 토큰_재발급_요청_확인() {
        UserJoinDto joinDto = createNewUser();
        UserLoginDto loginDto = new UserLoginDto(userEmail, userPassword);
        userService.joinNewUserFromDefault(joinDto);
        TokenDto tokenDto = userService.login(loginDto);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", tokenDto.getRefreshToken());

        TokenDto result = userService.refreshToken(request);

        assertThat(result).isNotNull();
    }

    @Test
    void 토큰_재발급_요청_실패() {
        UserJoinDto joinDto = createNewUser();
        UserLoginDto loginDto = new UserLoginDto(userEmail, userPassword);
        userService.joinNewUserFromDefault(joinDto);
        TokenDto tokenDto = userService.login(loginDto);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        assertThatThrownBy(() -> {
            userService.refreshToken(request);
        }).isInstanceOf(InvalidRefreshTokenException.class);
    }

    @Test
    void 유저_정보_확인() {
        UserJoinDto joinDto = createNewUser();
        userService.joinNewUserFromDefault(joinDto);
        UserData user = userDataRepository.findUserByEmail(userEmail);

        GetUserDto result = userService.getUserData(new GetUserDataDto(user));

        assertThat(result.getUserEmail()).isEqualTo(user.getEmail());
        assertThat(result.getUserType()).isEqualTo(user.getUserType().toString());
        assertThat(result.getUsername()).isEqualTo(user.getUserName());
        assertThat(result.getSsoType()).isEqualTo(user.getProvider().toString());
    }

    @Test
    void 유저_삭제_확인() {
        UserJoinDto joinDto = createNewUser();
        userService.joinNewUserFromDefault(joinDto);
        UserData user = userDataRepository.findUserByEmail(userEmail);

        DeleteUserDto deleteDto = new DeleteUserDto(user, userEmail);
        APIMessage apiMessage = userService.deleteUser(deleteDto);
        assertThat(apiMessage.getMessage()).isEqualTo(APIServerMessage.회원탈퇴_성공.getMessage());

        assertThatThrownBy(() -> {
            userDataRepository.findUserByEmail(userEmail);
        }).isInstanceOf(NotExistUserData.class);
    }

    UserJoinDto createNewUser() {
        return new UserJoinDto(userEmail, "admin", userPassword, "OFFSPRING", "DEFAULT");
    }
}
