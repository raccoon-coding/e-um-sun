package eumsun.backend.dto.parameter;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateTokenDto {
    private String userEmail;
    private Integer refreshCount;
}
