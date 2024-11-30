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
@Document(collection = "connection")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Connection extends DateTime {
    @Id
    private String id;

    @Field(name = "offspring_id")
    private String user;

    @Field(name = "parent_id")
    private String parent;

    private ConnectionState status;

    private String randomMixStr;

    @Builder
    public Connection(String offspringId, String parentId, String str) {
        this.user = offspringId;
        this.parent = parentId;
        this.status = ConnectionState.PENDING;
        this.randomMixStr = str;
    }

    public void acceptConnect() {
        this.status = ConnectionState.ACCEPTED;
    }
}
