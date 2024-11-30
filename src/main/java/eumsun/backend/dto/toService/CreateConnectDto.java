package eumsun.backend.dto.toService;

import eumsun.backend.domain.UserData;

public record CreateConnectDto(UserData parent, String offspringEmail){
}
