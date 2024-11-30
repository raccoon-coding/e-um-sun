package eumsun.backend.controller.exceptionController;

import eumsun.backend.dto.api.API;
import eumsun.backend.dto.api.APIErrorMessage;
import eumsun.backend.exception.controller.InvalidRefreshTokenException;
import eumsun.backend.exception.controller.NotEqualUserPasswordException;
import eumsun.backend.exception.controller.NotHaveToken;
import eumsun.backend.exception.controller.OverRefreshCountException;
import eumsun.backend.exception.controller.RefreshTokenRedirect;
import eumsun.backend.exception.repository.ExistUserData;
import eumsun.backend.exception.repository.NotExistUserData;
import eumsun.backend.exception.service.NotMatchUserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "eumsun.backend.controller")
public class NotTokenControllerException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RefreshTokenRedirect.class)
    public API<String> accessTokenExpiredExHandler(RefreshTokenRedirect e) {
        log.error("[RefreshTokenRedirect] ex = {}", e.getMessage());
        return new API<>(APIErrorMessage.토큰_재로그인);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotHaveToken.class)
    public API<String> NotHaveTokenExHandler(NotHaveToken e) {
        log.error("[NotHaveToken] ex = {}", e.getMessage());
        return new API<>(APIErrorMessage.토큰_요청);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OverRefreshCountException.class)
    public API<String> refreshTokenCountOverExHandler(OverRefreshCountException e) {
        log.error("[OverRefreshCountException] ex = {}", e.getMessage());
        return new API<>(APIErrorMessage.토큰_만료);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistUserData.class)
    public API<String> userDuplicationExHandle(ExistUserData e) {
        log.error("[ExistUserData] ex = {}", e.getMessage());
        return new API<>(APIErrorMessage.가입실패);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistUserData.class)
    public API<String> userNotFoundExHandle(NotExistUserData e) {
        log.error("[NotExistUserData] ex : {}", e.getMessage());
        return new API<>(APIErrorMessage.유저찾기실패);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotMatchUserType.class)
    public API<String> userTypeNotMatchExHandle(NotMatchUserType e) {
        log.error("[NotMatchUserType] ex : {}", e.getMessage());
        return new API<>(APIErrorMessage.유저타입에러);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotEqualUserPasswordException.class)
    public API<String> userPasswordNotEqualExHandler(NotEqualUserPasswordException e) {
        log.error("[NotEqualUserPasswordException] ex : {}", e.getMessage());
        return new API<>(APIErrorMessage.비밀번호에러);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public API<String> invalidRefreshTokenExHandler(InvalidRefreshTokenException e) {
        log.error("[InvalidRefreshTokenException] ex : {}", e.getMessage());
        return new API<>(APIErrorMessage.다른_토큰);
    }
}
