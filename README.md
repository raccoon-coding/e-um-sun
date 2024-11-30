<h1>
  e-um-sun backend server <img src="readmeImage/eumsun.jpeg" alt="eumsun logo" width="35" style="margin-left: 10px;"/>
</h1>

---

## 이음선 (e-um-sun) API 서버!
이음선(e-um-sun)은 경계선 지능 장애를 가진 사용자를 돕기 위해 개발된 어플리케이션입니다. 이 API 서버는 사용자들이 사회적으로 받아들여질 수 있는 행동을 판단하기 어려운 상황에서, GPT를 통해 그 행동이 사회적으로 적절한지, 위험한지, 또는 법적으로 문제가 될 수 있는지를 분석해주는 역할을 합니다.

사용자가 위험한 행동을 시도할 경우, 미리 등록된 보호자에게 자동으로 알림을 보내어 보호자가 사전에 조치할 수 있도록 돕습니다. 이 앱의 이름은 사용자와 보호자를 연결해주는 '선'이라는 의미에서 '이음선'으로 명명되었습니다.

---

## 설치 및 설정 (Installation and Setup)

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/e-um-sun-backend.git
   ```

2. **Set up environment variables**:
    ```bash 
    cd backend/src/main
    ```
   make 'resources/application.yml' file
   ```yaml
    server:
       address: server_computer_ip # ip default localhost or 127.0.0.1
       port: server port # default 8080
       servlet:
          context-path: /
          encoding:
             charset: UTF-8
             enabled: true
             force: true

    spring:
       data:
          mongodb:
             host: database host # default localhost
             port: database port # default 27017
             url: database_name://database_username:database_userpassword@database_name:database_port/database_name?authSource=admin
       jwt:
          secretKey: random 32bit string 

    openai:
       secret key: gpt_api_key

    openid:
       kakao: kakao_OpenID_AUD
       google: google_OpenID_AUD
       apple: apple_OpenID_AUD

    firebase:
       project-id: e-um-sun # Firebase 프로젝트 ID
       api-url: firebase_api_url # Firebase API URL
       firebase-json: firebase_json_file_path # Firebase 설정 JSON 파일 경로
       database-url: firebase_database_url # Firebase 데이터베이스 URL
    
    pay:
       admin-key:  # app admin key
    ```

3. **Set up docker-compose environment variables**:
   ```bash
   cd docker
   ```
   make '.env' file
   ```bash
   MONGODB_USER=mongo_database_username # MongoDB 사용자 이름
   MONGODB_PASSWORD=mongo_database_password # MongoDB 사용자 비밀번호
   MONGODB_DATA_URL=mongo_database_save_path # MongoDB 데이터 저장 경로
   ```

4. **Run the application:**:
   ```bash
   docker compose up --build
   ```
   
---

## 기술 스택 (Technology Stack)
- **Spring Boot**
- **Spring Security**
- **Spring AOP**
- **MongoDB**
- **Oauth2 & JWT**
- **Docker**
- **OpenAI API**
- **Swagger-Ui**
- **SSE (Server-Sent Events)**
- **Firebase**

---

## API 문서 (API Documentation)
- 이 프로젝트의 API 문서는 Swagger-UI를 통해 자동으로 생성되며, **"http://server_address:server_port/swagger-ui/index.html"** 에서 접근할 수 있습니다.

---
