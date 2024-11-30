package eumsun.backend.repository.repositoryImpl;

import eumsun.backend.domain.Connection;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.exception.repository.ExistUserData;
import eumsun.backend.exception.repository.NotExistConnection;
import eumsun.backend.repository.mongoRepository.UserDataRepository;
import eumsun.backend.exception.repository.NotExistUserData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDataRepositoryValid {
    private final UserDataRepository repository;

    public void patch(UserData user) {
        repository.save(user);
    }

    public void saveNewUserData(UserData newUserData) {
        try {
            findUserByEmail(newUserData.getEmail());
            throw new ExistUserData(APIErrorMessage.가입실패.getMessage());
        } catch(NotExistUserData e) {
            repository.save(newUserData);
        }
    }

    public void changeUserPassword(UserData userData, String password) {
        userData.changeUserPassword(password);
        repository.save(userData);
    }

    public Connection findConnectionByUserData(UserData user) {
        Connection connection = user.getConnection();
        if(connection == null) {
            throw new NotExistConnection("아직 유저가 보호자와 연결되지 않았습니다. 먼저 보호자와 연결을 시도해주세요");
        }
        return connection;
    }

    public UserData findUserById(String id) {
        Optional<UserData> optionalUserData = repository.findById(id);

        if(optionalUserData.isPresent()){
            return optionalUserData.get();
        }
        throw new NotExistUserData(APIErrorMessage.유저찾기실패.getMessage());
    }

    public UserData findUserByEmail(String userEmail) {
        Optional<UserData> optionalUserData = repository.findByEmail(userEmail);

        if(optionalUserData.isPresent()){
            return optionalUserData.get();
        }
        throw new NotExistUserData(APIErrorMessage.유저찾기실패.getMessage());
    }

    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }
}
