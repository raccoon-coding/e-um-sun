package eumsun.backend.config.log;

import eumsun.backend.domain.UserData;
import eumsun.backend.dto.toService.CreateLogDto;
import eumsun.backend.service.LogService;
import eumsun.backend.util.UserDataUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    private final LogService logService;

    @Around("eumsun.backend.config.log.LogPointcut.notToken()")
    public Object beforeAdviceNotTokenLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return tryLog(joinPoint, this::notTokenLogging);
    }

    @Around("eumsun.backend.config.log.LogPointcut.token()")
    public Object beforeAdviceTokenLog(ProceedingJoinPoint joinPoint) throws Throwable {
        return tryLog(joinPoint, this::tokenLogging);
    }

    private Object tryLog(ProceedingJoinPoint joinPoint, Consumer<ProceedingJoinPoint> logMethod) throws Throwable {
        Object result = joinPoint.proceed();
        logMethod.accept(joinPoint);
        return result;
    }

    private void notTokenLogging(ProceedingJoinPoint joinPoint) {
        String requestBody = getRequestBody();
        viewLog(joinPoint, requestBody);
        createLog(joinPoint, requestBody);
    }

    private void tokenLogging(ProceedingJoinPoint joinPoint) {
        String requestBody = getRequestBody();
        UserData loginUser = UserDataUtil.getUserData();

        viewUserLog(loginUser);
        viewLog(joinPoint, requestBody);
        createUserLog(joinPoint, loginUser, requestBody);
    }

    private String getRequestBody() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        return new String(((ContentCachingRequestWrapper) request).getContentAsByteArray());
    }

    private void createLog(ProceedingJoinPoint thisJoinPoint, String requestBody) {
        CreateLogDto dto = CreateLogDto.of(thisJoinPoint.getSignature().getName(), requestBody);
        logService.createLog(dto);
    }

    private void createUserLog(ProceedingJoinPoint thisJoinPoint, UserData loginUser, String requestBody) {
        CreateLogDto dto = new CreateLogDto(loginUser.getId(), thisJoinPoint.getSignature().getName(), requestBody);
        logService.createLog(dto);
    }

    private void viewUserLog(UserData loginUser) {
        log.info("Login User Id : {}", loginUser.getId());
    }

    private void viewLog(ProceedingJoinPoint thisJoinPoint, String requestBody) {
        log.info("user Request Data : {}", requestBody);
        log.info("log signature : {}", thisJoinPoint.getSignature().getName());
    }
}
