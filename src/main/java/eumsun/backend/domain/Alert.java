package eumsun.backend.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "alert")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Alert extends DateTime {
    @Id
    private String id;

    @Field(name = "offspring_id")
    private String userId;

    @Field(name = "parent_id")
    private String parentId;

    @Field(name = "content")
    private String content;

    @Builder
    public Alert(String offspringId, String parentId, String content) {
        this.userId = offspringId;
        this.parentId = parentId;
        this.content = content;
    }
}
