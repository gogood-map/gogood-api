package gogood.gogoodapi.models.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EnvConfig{

    public static String jdbcUrl;
    public static String jdbcUsername;
    public static String jdbcPassword;
    public void connectToDatabase() {
        Dotenv dotenv = Dotenv.load();
        jdbcUrl = dotenv.get("JDBC_URL");
        jdbcUsername = dotenv.get("JDBC_USERNAME");
        jdbcPassword = dotenv.get("JDBC_PASSWORD");

    }
}
