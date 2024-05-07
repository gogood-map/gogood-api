package gogood.gogoodapi.configuration;

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
        Server server = new Server();
        server.setUrl("https://gogood.brazilsouth.cloudapp.azure.com");
        return new OpenAPI().servers(List.of(server));
    }
}
