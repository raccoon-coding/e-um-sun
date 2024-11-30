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

import java.util.ArrayList;
import java.util.List;

@Getter
@Document(collection = "conversation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Conversation extends DateTime {
    @Id
    private String id;

    @Field(name = "offspring_id")
    private String userId;

    @DBRef(lazy = true)
    @Field(name = "messages_id")
    private List<Message> messages;

    @Builder
    public Conversation(String offspringId) {
        this.userId = offspringId;
        this.messages = new ArrayList<>(20);
    }

    public void useGPTMessage(Message message) {
        this.messages.add(message);
    }
}
