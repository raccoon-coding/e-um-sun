package eumsun.backend.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Document(collection = "purchase")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Purchase extends DateTime {
    @Id
    private String id;

    @Field(name = "offspring_id")
    private String userId;

    @Field(name = "purchase_cost")
    private int cost;

    @Field(name = "purchase_token")
    private int token;
}
