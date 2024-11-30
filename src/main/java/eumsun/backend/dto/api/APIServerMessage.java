package eumsun.backend.dto.api;

public enum APIServerMessage implements APIMessage {
    회원가입_성공("회원가입에 성공했습니다.\n 로그인 해주세요", 200),
    회원탈퇴_성공("회원탈퇴에 성공했습니다. \n 그동안 저희 서비스를 이용해주셔서 감사합니다.", 200),
    요청_성공("Success Request \n", 200),
    비밀번호_변경_성공("비밀번호 변경 완료했습니다. 다시 로그인해주세요.", 200),
    로그인_성공("로그인 성공했습니다.", 200),
    토큰_재발급_성공("토큰 재발급 성공했습니다.", 200),
    연결_성공("보호자와 유저의 연결이 성공했습니다.", 200)
    ;
    private final String message;
    private final Integer code;

    APIServerMessage(String message, Integer code){
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
