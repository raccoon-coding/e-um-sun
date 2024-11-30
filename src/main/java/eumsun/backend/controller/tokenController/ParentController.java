package eumsun.backend.controller.tokenController;

import eumsun.backend.domain.UserData;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.request.controller.ParentConnectDto;
import eumsun.backend.dto.toService.CreateConnectDto;
import eumsun.backend.dto.toService.GetRandomStringDto;
import eumsun.backend.service.ParentService;
import eumsun.backend.util.UserDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/parent")
@RequiredArgsConstructor
public class ParentController {
    private final ParentService parentService;

    @PostMapping("/connection")
    public API<String> connectOffspring(@Validated @RequestBody ParentConnectDto dto) {
        UserData parent = UserDataUtil.getUserData();
        return parentService.connectOffspring(new CreateConnectDto(parent, dto.getOffspringEmail()));
    }

    @GetMapping("/connect/getString")
    public API<String> getConnectionString() {
        UserData parent = UserDataUtil.getUserData();
        return parentService.getString(new GetRandomStringDto(parent));
    }
}
