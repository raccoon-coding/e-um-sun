package eumsun.backend.dto;

import eumsun.backend.domain.Connection;
import eumsun.backend.domain.UserData;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SaveConnectionDto {

    private UserData parent;
    private UserData offspring;
    private Connection connection;
}
