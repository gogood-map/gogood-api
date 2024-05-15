package gogood.gogoodapi.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

@Getter
public class JdbcConfig {

    private JdbcTemplate conexaoDoBanco;

    public JdbcConfig() {
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("JDBC_URL");
        String username = dotenv.get("JDBC_USERNAME");
        String password = dotenv.get("JDBC_PASSWORD");
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        conexaoDoBanco = new JdbcTemplate(dataSource);
    }
}