package eumsun.backend.service;

import eumsun.backend.domain.Connection;
import eumsun.backend.domain.UserData;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.toService.ConnectUserDto;
import eumsun.backend.exception.service.StringMismatchException;
import eumsun.backend.repository.repositoryImpl.ConnectionRepositoryValid;
import eumsun.backend.repository.repositoryImpl.UserDataRepositoryValid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OffspringService {
    private final ConnectionRepositoryValid connectionRepository;

    @Transactional
    public API<String> connectParent(ConnectUserDto dto) {
        UserData offspring = dto.offspring();
        Connection connection = offspring.getConnection();

        validRandomString(dto.randomStr(), connection);

        return connecting(connection);
    }

    private void validRandomString(String randomStr, Connection connection) {
        if(!Objects.equals(connection.getRandomMixStr(), randomStr)){
            throw new StringMismatchException(APIErrorMessage.랜덤_요청_에러.getMessage());
        }
    }

    @NotNull
    private API<String> connecting(@NotNull Connection connection) {
        connection.acceptConnect();

        connectionRepository.patch(connection);

        return new API<>(APIServerMessage.연결_성공);
    }
}
