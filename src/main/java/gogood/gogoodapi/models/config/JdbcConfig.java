package gogood.gogoodapi.models.config;
import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

@Getter
public class JdbcConfig {
    private JdbcTemplate conexaoDoBanco;

    public JdbcConfig() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://104.41.27.114:3306/GoGood");
        dataSource.setUsername("root");
        dataSource.setPassword("Root@123");
        conexaoDoBanco = new JdbcTemplate(dataSource);
    }
}