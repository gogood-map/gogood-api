package gogood.gogoodapi.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

@Getter
public class JdbcConfig {
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