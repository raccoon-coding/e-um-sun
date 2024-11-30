package eumsun.backend.dto.api;

import lombok.Getter;

@Getter
public class API<T> {
    private final T data;
    private final Integer code;
    private final String message;

    public API(T data, APIMessage apiMessage) {
        this.data = data;
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
    }

    public API(APIMessage apiMessage) {
        this.data = null;
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
    }
}
