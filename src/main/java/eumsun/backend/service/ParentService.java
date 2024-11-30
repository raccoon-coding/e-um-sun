package eumsun.backend.service;

import eumsun.backend.domain.Connection;
import eumsun.backend.domain.UserData;
import eumsun.backend.domain.UserType;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.toService.CreateConnectDto;
import eumsun.backend.dto.toService.GetRandomStringDto;
import eumsun.backend.exception.service.DuplicateConnectionException;
import eumsun.backend.exception.service.NotMatchOffspringUser;
import eumsun.backend.exception.service.ParentNotConnectedException;
import eumsun.backend.repository.repositoryImpl.ConnectionRepositoryValid;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentService {
    private final UserDataRepositoryValid userDataRepository;
    private final ConnectionRepositoryValid connectionRepository;

    private final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transactional
    public API<String> connectOffspring(CreateConnectDto dto) {
        UserData parent = dto.parent();
        UserData offspring = userDataRepository.findUserByEmail(dto.offspringEmail());

        validUserType(offspring);
        validConnectState(parent, offspring);
        Connection connection = connecting(offspring, parent);

        return new API<>(connection.getRandomMixStr(), APIServerMessage.요청_성공);
    }

    public API<String> getString(GetRandomStringDto dto) {
        UserData parent = dto.parent();
        Connection parentConnection = parent.getConnection();

        validConnect(parentConnection);
        return new API<>(parentConnection.getRandomMixStr(), APIServerMessage.요청_성공);
    }

    private void validUserType(UserData offspring) {
        if (!offspring.getUserType().equals(UserType.OFFSPRING)) {
            throw new NotMatchOffspringUser(APIErrorMessage.유저_타입_에러.getMessage());
        }
    }

    private void validConnectState(UserData parent, UserData offspring) {
        if (parent.getConnection() != null || offspring.getConnection() != null) {
            throw new DuplicateConnectionException(APIErrorMessage.중복_연결.getMessage());
        }
    }

    private void validConnect(Connection parentConnection) {
        if(parentConnection == null){
            throw new ParentNotConnectedException(APIErrorMessage.연결_요청_에러.getMessage());
        }
    }

    private Connection connecting(@NotNull UserData offspring, @NotNull UserData parent) {
        Connection connection = createConnect(offspring, parent);

        parent.connectUser(connection);
        offspring.connectUser(connection);

        connectionRepository.createConnect(connection);
        userDataRepository.patch(parent);
        userDataRepository.patch(offspring);
        return connection;
    }

    private Connection createConnect(UserData offspring, UserData parent) {
        return Connection.builder()
                .offspringId(offspring.getId())
                .parentId(parent.getId())
                .str(generateRandomMixStr())
                .build();
    }

    private String generateRandomMixStr() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
