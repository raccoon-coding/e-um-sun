package eumsun.backend.service;

import eumsun.backend.config.jwt.JwtProvider;
import eumsun.backend.domain.Connection;
import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.SsoType;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.response.TokenDto;
import eumsun.backend.dto.request.controller.UserJoinDto;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.request.controller.UserLoginDto;
import eumsun.backend.dto.response.GetUserDto;
import eumsun.backend.dto.toService.ChangePasswordDto;
import eumsun.backend.dto.toService.CreateTokenDto;
import eumsun.backend.dto.toService.DeleteUserDto;
import eumsun.backend.dto.toService.GetUserDataDto;
import eumsun.backend.exception.controller.InvalidRefreshTokenException;
import eumsun.backend.exception.controller.NotEqualOldPassword;
import eumsun.backend.exception.controller.NotEqualUserPasswordException;
import eumsun.backend.exception.service.NotMatchUserType;
import eumsun.backend.repository.repositoryImpl.ConnectionRepositoryValid;
import eumsun.backend.repository.repositoryImpl.ConversationRepositoryValid;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDataRepositoryValid userDataRepository;
    private final ConnectionRepositoryValid connectionRepository;
    private final ConversationRepositoryValid conversationRepository;

    @Transactional
    public APIMessage joinNewUserFromDefault(UserJoinDto dto) {
        String encodePassword = bCryptPasswordEncoder.encode(dto.getPassword());

        tryJoinNewUser(dto, encodePassword);

        return APIServerMessage.회원가입_성공;
    }

    public TokenDto login(UserLoginDto dto) {
        String email = dto.getEmail();
        UserData user = userDataRepository.findUserByEmail(email);

        if(bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            CreateTokenDto createTokenDto = new CreateTokenDto(user.getId(), 0);
            return jwtProvider.createToken(createTokenDto);
        }
        throw new NotEqualUserPasswordException(APIErrorMessage.비밀번호에러.getMessage());
    }

    public TokenDto refreshToken(HttpServletRequest request) {
        String jwt = jwtProvider.getToken(request);
        if(jwtProvider.validateRefreshToken(jwt)) {
            String userId = jwtProvider.getRefreshUserPk(jwt);
            Integer count = jwtProvider.getRefreshCount(jwt);
            return jwtProvider.createToken(new CreateTokenDto(userId, count));
        }
        throw new InvalidRefreshTokenException(APIErrorMessage.재발급_토큰_에러.getMessage());
    }

    public GetUserDto getUserData(GetUserDataDto dto) {
        return new GetUserDto(dto.user());
    }

    public UserData findParent(UserData offspring) {
        Connection connection = userDataRepository.findConnectionByUserData(offspring);
        String parentId = connection.getParent();
        return userDataRepository.findUserById(parentId);
    }

    @Transactional
    public APIMessage deleteUser(DeleteUserDto dto) {
        UserData user = dto.user();

        tryDeleteConversation(user);
        tryDeleteConnection(user);
        userDataRepository.deleteByEmail(dto.userEmail());

        return APIServerMessage.회원탈퇴_성공;
    }

    @Transactional
    public APIMessage changeUserPassword(ChangePasswordDto dto) {
        UserData user = dto.user();

        if(bCryptPasswordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            userDataRepository.changeUserPassword(user, bCryptPasswordEncoder.encode(dto.newPassword()));
            return APIServerMessage.비밀번호_변경_성공;
        }
        throw new NotEqualOldPassword(APIErrorMessage.비밀번호_변경_에러.getMessage());
    }

    private void tryJoinNewUser(UserJoinDto dto, String encodePassword) {
        if(!ValidOffspring(dto) && !ValidParent(dto) && !ValidDevelop(dto)) {
            throw new NotMatchUserType(APIErrorMessage.유저타입에러.getMessage());
        }
        joinNewOffspring(dto, encodePassword);
        joinNewParent(dto, encodePassword);
        joinNewDevelop(dto, encodePassword);
    }

    private Boolean ValidOffspring(UserJoinDto dto) {
        return dto.getUserType().equals(UserType.OFFSPRING.toString());
    }

    private Boolean ValidParent(UserJoinDto dto) {
        return dto.getUserType().equals(UserType.PARENT.toString());
    }

    private Boolean ValidDevelop(UserJoinDto dto) {
        return dto.getUserType().equals(UserType.DEVELOP.toString());
    }

    private void joinNewOffspring(UserJoinDto dto, String encodePassword) {
        if(dto.getUserType().equals(UserType.OFFSPRING.toString())){
            UserData newOffspring = UserData.builder()
                    .email(dto.getEmail())
                    .userName(dto.getUserName())
                    .password(encodePassword)
                    .provider(SsoType.DEFAULT)
                    .userType(UserType.OFFSPRING).build();
            userDataRepository.saveNewUserData(newOffspring);

            UserData user = userDataRepository.findUserByEmail(dto.getEmail());
            user.createConversation();
            conversationRepository.createConversation(user.getConversation());
            userDataRepository.patch(user);
        }
    }

    private void joinNewParent(UserJoinDto dto, String encodePassword) {
        if(dto.getUserType().equals(UserType.PARENT.toString())){
            UserData parent = UserData.builder()
                    .email(dto.getEmail())
                    .userName(dto.getUserName())
                    .password(encodePassword)
                    .provider(SsoType.DEFAULT)
                    .userType(UserType.PARENT).build();
            userDataRepository.saveNewUserData(parent);
        }
    }

    private void joinNewDevelop(UserJoinDto dto, String encodePassword) {
        if(dto.getUserType().equals(UserType.DEVELOP.toString())){
            UserData developer = UserData.builder()
                    .email(dto.getEmail())
                    .userName(dto.getUserName())
                    .password(encodePassword)
                    .provider(SsoType.DEFAULT)
                    .userType(UserType.DEVELOP).build();
            userDataRepository.saveNewUserData(developer);
        }
    }

    private void tryDeleteConversation(UserData user) {
        if(user.getConversation() != null) {
            Conversation conversation = user.getConversation();
            deleteConversation(conversation);
        }
    }

    private void deleteConversation(Conversation conversation) {
        conversationRepository.deleteConversation(conversation);
    }

    private void tryDeleteConnection(UserData user) {
        if(user.getConnection() != null) {
            Connection connection = user.getConnection();
            String parentId = connection.getParent();
            UserData parent = userDataRepository.findUserById(parentId);

            deleteConnection(user, parent, connection);
        }
    }

    private void deleteConnection(UserData user, UserData parent, Connection connection) {
        user.deleteConnection();
        parent.deleteConnection();

        userDataRepository.patch(user);
        userDataRepository.patch(parent);
        connectionRepository.deleteConnect(connection);
    }
}
