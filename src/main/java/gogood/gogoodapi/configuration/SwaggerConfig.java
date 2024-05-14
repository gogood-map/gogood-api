package gogood.gogoodapi.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        Dotenv dotenv = Dotenv.load();
        Server server = new Server();
        server.setUrl(dotenv.get("SWAGGER_URL"));
        return new OpenAPI().servers(List.of(server)).info(new Info().title("GoGood API").version("1.0"));
    }
}
