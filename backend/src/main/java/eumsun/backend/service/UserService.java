package eumsun.backend.service;

import eumsun.backend.config.auth.PrincipalDetails;
import eumsun.backend.domain.Connection;
import eumsun.backend.domain.Conversation;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.repository.ConnectionRepository;
import eumsun.backend.repository.ConversationRepository;
import eumsun.backend.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserDataRepository userDataRepository;
    private final ConnectionRepository connectionRepository;
    private final ConversationRepository conversationRepository;

    @Transactional
    public String join(UserData userData) {

        try{
            validateDuplicateUserData(userData.getEmail());
            if(userData.getUserType().equals(UserType.OFFSPRING)){
                conversationRepository.save(userData.getConversation());
            }
            userDataRepository.save(userData);
            return "회원 가입이 완료되었습니다.";
        }
        catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    @Transactional
    public String deleteUser(UserData deleteUserData) {

        userDataRepository.delete(deleteUserData);
        return "회원 정보 삭제가 완료되었습니다.";
    }

    @Transactional
    public void deleteConversation(Conversation conversation) {

        conversationRepository.delete(conversation);
    }

    @Transactional
    public void deleteConnection(Connection connection, String userID){

        String offspringId = connection.getUser();
        String parentId = connection.getParent();
        if(offspringId.equals(userID)){
            UserData parent = validUserData(parentId);
            parent.connectUser(null);
            userDataRepository.save(parent);
        }
        else{
            UserData offspring = validUserData(parentId);
            offspring.connectUser(null);
            userDataRepository.save(offspring);
        }
        connectionRepository.delete(connection);
    }

    public UserData findUserDataByEmail(String userEmail) {
        Optional<UserData> userDataOptional = userDataRepository.findByEmail(userEmail);
        if(userDataOptional.isPresent()){
            return  userDataOptional.get();
        }
        else {
            throw new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다.");
        }
    }

    public String findConnectionEmailByUserData(UserData userData) {

        Connection connection = userData.getConnection();
        if(connection == null){
            return "연결된 유저가 없습니다.";
        }
        String offspringId = connection.getUser();

        if(Objects.equals(offspringId, userData.getId())){

            return validUserData(connection.getParent()).getEmail();
        }

        return validUserData(offspringId).getEmail();
    }

    public UserData validUserData(String userId) {
        Optional<UserData> user = userDataRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalArgumentException( "해당하는 유저가 존재하지 않습니다.");
        }
        return user.get();
    }

    public String findUserDataByToken() {

        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principalDetails.getUserEmail();
    }

    public String findParentIdByOffspringId(String offspringId) {

        UserData offspring = validUserData(offspringId);
        Connection connection = offspring.getConnection();
        return connection.getParent();
    }

    private void validateDuplicateUserData(String userEmail) {
        Optional<UserData> userDataOptional = userDataRepository.findByEmail(userEmail);
        if(userDataOptional.isPresent()){
            throw new IllegalArgumentException("해당하는 유저가 존재합니다.");
        }
    }
}
