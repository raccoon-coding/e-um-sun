package eumsun.backend.repository.customInterface;

import eumsun.backend.domain.UserData;

import java.util.Optional;

public interface UserDataCustomRepository {
    Optional<UserData> findByEmail(String email);
}
