package eumsun.backend.config.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogPointcut {
    @Pointcut("execution(* eumsun.backend.controller.NotTokenController.*(..))")
    public void notToken(){
    }

    @Pointcut("execution(* eumsun.backend.controller..*(..))")
    public void controller() {
    }

    @Pointcut("execution(* eumsun.backend.controller.exceptionController..*(..))")
    public void exceptionController() {
    }

    @Pointcut("controller() && !notToken() && !exceptionController()")
    public void token() {
    }
}
