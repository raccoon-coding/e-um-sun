package eumsun.backend.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Document(collection = "user_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 레포지토리에서 Builder에 없는 데이터를 초기화 하기 위해서 기본 생성자를 넣어주는 어노테이션이다.
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserData extends DateTime {
    @Id
    private String id;

    @Field(name = "user_password")
    private String password;

    @Field
    private LocalDateTime updateAtPassword;

    @Field(name = "user_name")
    private String userName;

    @Indexed(unique=true)
    @Field(name = "email")
    private String email;
    // seoType 연결
    @Field(name = "sso_type")
    private SsoType provider;

    @Field(name = "user_type")
    private UserType userType;

    @DBRef
    @Field(name = "connection_id")
    private Connection connection;

    @DBRef(lazy = true)
    @Field(name = "user_conversation")
    private Conversation conversation;

    @Field(name = "user_token")
    private int token;

    @Field(name = "firebase_token")
    private String firebaseToken;

    @Builder
    public UserData(String password, String userName, String email, SsoType provider, UserType userType) {
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.provider = provider;
        this.userType = userType;
    }

    public void changeUserPassword(String password) {
        this.password = password;
        this.updateAtPassword = LocalDateTime.now();
    }

    public void connectUser(Connection connection) {
        this.connection = connection;
    }

    public void createConversation() {
        if(this.userType.equals(UserType.OFFSPRING)){
            this.conversation = Conversation.builder()
                    .offspringId(this.id)
                    .build();
        }
    }

    public void deleteConnection() {
        this.connection = null;
    }

    public void deleteConversation() {
        this.conversation = null;
    }

    public void postConstructFirebase(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return token == userData.token && Objects.equals(id, userData.id) && Objects.equals(password, userData.password) && Objects.equals(userName, userData.userName) && Objects.equals(email, userData.email) && provider == userData.provider && userType == userData.userType && Objects.equals(connection, userData.connection) && Objects.equals(conversation, userData.conversation) && Objects.equals(firebaseToken, userData.firebaseToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, userName, email, provider, userType, connection, conversation, token, firebaseToken);
    }
}
