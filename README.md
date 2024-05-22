# e-um-sun-backend

1. token에서 JWS를 밖에 노출시키지 않기 위해서 내부에서만 처리하고 싶은데, 이렇게 되면 같은 연산을 2번 진행하게 된다.

## OpenAi API 문서
- https://github.com/TheoKanning/openai-java

## Swagger-UI URL
http://localhost:8080/swagger-ui/index.html

### OpenAI Token 라이브러리
- https://jtokkit.knuddels.de/docs/getting-started/recipes/chatml

### JWT 토큰 자료 저장 방식 참고자료
- https://velog.io/@whdgh9595/Spring-%EC%9D%B8%EC%A6%9D-%EC%82%AC%EC%9A%A9%EC%9E%90%EC%9D%98-%EC%A0%95%EB%B3%B4-%EC%A0%80%EC%9E%A5
- https://github.com/jwtk/jjwt

### api문서

- https://app.gitbook.com/o/oD8ic56f4RM5mpnLkcbv/s/1KlJp2OZEdHMyZzuAUEA/~/changes/1/eumsun_backend/join-user

### Oauth (OpenIdConnect) Api 문서
Kakao : https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code

Naver : https://developers.naver.com/docs/login/devguide/devguide.md#3-4-5-%EC%A0%91%EA%B7%BC-%ED%86%A0%ED%81%B0%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%ED%94%84%EB%A1%9C%ED%95%84-api-%ED%98%B8%EC%B6%9C%ED%95%98%EA%B8%B0

Google : https://developers.google.com/identity/gsi/web/guides/verify-google-id-token?hl=ko

Apple : https://developer.apple.com/documentation/sign_in_with_apple/generate_and_validate_tokens

## Open ID 참고 자료
- https://aodtns.tistory.com/124
- https://devnm.tistory.com/35
- https://brorica.tistory.com/250

## SSE 참고 자료
- https://akku-dev.tistory.com/84
- https://akku-dev.tistory.com/76

### FireBase 관련 참고 자료
- https://c-king.tistory.com/604

### Docker 관련 참고 자료
- https://velog.io/@jjinwo0/Docker-Docker-Git-Action%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%98%EC%97%AC-Spring-Boot-Project-CICD-%EA%B5%AC%EC%B6%95
- 

### MongoDB 참고 자료
- https://crazy-horse.tistory.com/88
- https://crazy-horse.tistory.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-MongoDB%EB%A1%9C-CRUD-%EC%8B%A4%EC%8A%B5%ED%95%B4%EB%B3%B4%EA%B8%B0

### 진행 과정
1. Firebase 결제, 알림 구현

### git secrete
1. secrets.APPLICATION_PROD -> application.yml파일을 넣어놓기
2. secrets.DOCKER_USERNAME -> docker username
3. secrets.DOCKER_PASSWORD -> docker userpassword
4. secrets.RAILWAY_TOKEN -> railway token
5. secrets.RAILWAY_PROJECT_ID -> railway project id

### Docker Spring boot 연동
1. docker 설치
2. backend/libs/Dockerfile 로 docker 설정하기(이때, 이름을 바꾸면 안된다.)
3. docker build -t springbootserver ./ 를 해당 backend/libs 경로에서 실행한다.
4. docker save -o springbootserver.tar springbootserver:latest 실행
5. docker load -i springbootserver.tar 실행

### MongoDB, Docker 연동
1. docker pull mongo 실행 
2. docker run --name mongodb -d -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=**** mongo

### Swagger UI + Spring DOC + Spring
- https://hogwart-scholars.tistory.com/entry/Spring-Boot-SpringDoc%EA%B3%BC-Swagger%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%B4-API-%EB%AC%B8%EC%84%9C%ED%99%94-%EC%9E%90%EB%8F%99%ED%99%94%ED%95%98%EA%B8%B0
- 