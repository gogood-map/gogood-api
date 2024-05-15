package gogood.gogoodapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories


public class GogoodApiApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.directory("/home/gogood")
				.load();

		SpringApplication.run(GogoodApiApplication.class, args);
	}


}
