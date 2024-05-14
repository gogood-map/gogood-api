package gogood.gogoodapi.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.validation.Valid;
import lombok.Getter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

@Getter
public class JdbcConfig {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    private JdbcTemplate conexaoDoBanco;

    public JdbcConfig() {
        Dotenv dotenv = Dotenv.load();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dotenv.get("JDBC_URL"));
        dataSource.setUsername(dotenv.get("JDBC_USERNAME"));
        dataSource.setPassword(dotenv.get("JDBC_PASSWORD"));
        conexaoDoBanco = new JdbcTemplate(dataSource);
    }
}