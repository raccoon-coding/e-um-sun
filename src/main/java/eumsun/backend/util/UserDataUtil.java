package eumsun.backend.util;

import eumsun.backend.config.security.PrincipalDetails;
import eumsun.backend.domain.UserData;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserDataUtil {
    public static UserData getUserData() {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return principalDetails.getUserData();
    }
}
