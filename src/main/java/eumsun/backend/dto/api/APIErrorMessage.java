package eumsun.backend.dto.api;

public enum APIErrorMessage implements APIMessage {
    가입실패("이미 가입되어 있습니다. 가입된 이메일로 로그인 해주세요.", 409),
    유저찾기실패("해당하는 유저 데이터가 존재하지 않습니다. 회원가입을 진행해주세요", 409),
    유저타입에러("유저 타입이 잘못되었습니다. 다시 입력주세요.", 408),
    비밀번호에러("비밀번호가 일치하지 않습니다. 다시 입력해주세요.", 409),
    비밀번호_변경_에러("이전 비밀번호가 일치하지 않습니다. 다시 입력해주세요.", 409),
    다른_토큰("저희 토큰이 아닙니다. 다시 로그인해주세요.", 408),
    재발급_토큰_에러("refresh token이 유효하지 않습니다. 재로그인 해주세요.", 408),
    토큰_재로그인("access 토큰이 만료되었습니다. 다시 로그인해주세요.", 303),
    토큰_만료("refresh 토큰이 만료되었습니다. 재 로그인 해주세요.", 408),
    중복_연결("이미 유저와 연결되어 있습니다.", 409),
    유저_타입_에러("연결을 요청한 유저는 대상자가 아닙니다. 다시 입력해주세요.", 409),
    연결_요청_에러("유저와의 연결을 요청하지 않았습니다. 요청을 먼저 해주세요", 409),
    랜덤_요청_에러("랜덤 문자가 일치하지 않습니다. 다시 입력해주세요.", 409),
    유저_변경_후_토큰_만료("비밀번호가 변경되었습니다. 다시 로그인 해주세요", 409),
    토큰_요청("토큰이 없습니다. 로그인을 다시 해주세요!", 408),
    유저_강제_종료("강제 종료되었습니다.", 408)
    ;

    private final String message;
    private final Integer code;

    APIErrorMessage(String message, Integer code){
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
