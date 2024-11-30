package eumsun.backend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.validation.constraints.NotNull;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "eumsun.backend.repository")
public class MongoDBConfig extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.url}")
    private String connectionUrl;

    @NotNull
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(connectionUrl);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @NotNull
    @Override
    protected String getDatabaseName() {
        return "mongodb";
    }
}
