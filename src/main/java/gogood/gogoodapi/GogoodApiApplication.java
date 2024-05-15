package gogood.gogoodapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableMongoRepositories


public class GogoodApiApplication {
	public static void main(String[] args) {
		String dotenvPath = "/home/gogood/.env";

		// Verificar se o arquivo .env existe antes de carregá-lo
		if (Files.exists(Paths.get(dotenvPath))) {
			Dotenv dotenv = Dotenv.configure()
					.directory("/home/gogood")
					.load();
		}
		SpringApplication.run(GogoodApiApplication.class, args);
	}


}
