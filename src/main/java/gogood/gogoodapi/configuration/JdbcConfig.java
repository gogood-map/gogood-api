package gogood.gogoodapi.configuration;

import lombok.Getter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

@Getter
public class JdbcConfig {
    private JdbcTemplate conexaoDoBanco;

    public JdbcConfig() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://gogood.brazilsouth.cloudapp.azure.com:3306/GoGood");
        dataSource.setUsername("root");
        dataSource.setPassword("Root@123");
        conexaoDoBanco = new JdbcTemplate(dataSource);
    }
}