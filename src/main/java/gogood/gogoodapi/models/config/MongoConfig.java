package gogood.gogoodapi.models.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    @Bean
    public static MongoClient mongoClient(){
        return MongoClients.create("mongodb://gogood:gogood24@191.239.123.2:27017");
    }
    @Bean
    public MongoOperations mongoTemplate() {
        return new MongoTemplate(mongoClient(), "gogood");
    }
}
