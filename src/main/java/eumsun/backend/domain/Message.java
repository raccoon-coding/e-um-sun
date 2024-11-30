package eumsun.backend.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends DateTime {
    @Id
    private String id;

    @Field(name = "content")
    private String content;

    @Field(name = "message_role")
    private String role;

    @DBRef
    @Field(name = "conversation_id")
    private Conversation conversation;

    @Field(name = "cost")
    private int cost;

    @Builder
    public Message(String role, String content, Integer cost, Conversation conversation) {
        this.role = role;
        this.content = content;
        this.cost = cost;
        this.conversation = conversation;
    }
}
