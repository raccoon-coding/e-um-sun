package eumsun.backend.controller.tokenController;

import eumsun.backend.domain.UserData;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.request.controller.UserRandomStringDto;
import eumsun.backend.dto.toService.ConnectUserDto;
import eumsun.backend.service.OffspringService;
import eumsun.backend.util.UserDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/offspring")
@RequiredArgsConstructor
public class OffspringController {
    private final OffspringService offspringService;

    @PatchMapping("/connection")
    public API<String> connectParent(@Validated @RequestBody UserRandomStringDto dto) {
        UserData offspring = UserDataUtil.getUserData();
        return offspringService.connectParent(new ConnectUserDto(offspring, dto.getRandomString()));
    }
}
