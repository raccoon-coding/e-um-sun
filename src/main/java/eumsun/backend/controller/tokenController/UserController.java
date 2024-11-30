package eumsun.backend.controller.tokenController;

import eumsun.backend.domain.UserData;
import eumsun.backend.dto.api.APIMessage;
import eumsun.backend.dto.api.APIServerMessage;
import eumsun.backend.dto.request.controller.UserChangePasswordDto;
import eumsun.backend.dto.request.controller.UserDeleteDto;
import eumsun.backend.dto.api.API;
import eumsun.backend.dto.response.GetUserDto;
import eumsun.backend.dto.toService.ChangePasswordDto;
import eumsun.backend.dto.toService.DeleteUserDto;
import eumsun.backend.dto.toService.GetUserDataDto;
import eumsun.backend.service.UserService;
import eumsun.backend.util.UserDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getUserData")
    public API<GetUserDto> getUserData() {
        UserData user = UserDataUtil.getUserData();
        GetUserDto result = userService.getUserData(new GetUserDataDto(user));

        return new API<>(result, APIServerMessage.요청_성공);
    }

    @PatchMapping("/changePassword")
    public API<String> changeUserPassword(@Validated @RequestBody UserChangePasswordDto dto) {
        UserData user = UserDataUtil.getUserData();
        ChangePasswordDto passwordDto = new ChangePasswordDto(user, dto.getOldPassword(), dto.getNewPassword());
        APIMessage apiMessage = userService.changeUserPassword(passwordDto);

        return new API<>(apiMessage);
    }

    @DeleteMapping("/delete")
    public API<String> deleteUserData(@Validated @RequestBody UserDeleteDto dto) {
        UserData user = UserDataUtil.getUserData();
        APIMessage apiMessage = userService.deleteUser(new DeleteUserDto(user, dto.getUserEmail()));

        return new API<>(apiMessage);
    }
}
