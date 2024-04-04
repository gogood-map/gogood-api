package gogood.gogoodapi.models.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcConfig {
    private JdbcTemplate conexaoDoBanco;

    public JdbcConfig() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://52.226.122.160:3306/GoGood");
        dataSource.setUsername("root");
        dataSource.setPassword("Root@123");
        conexaoDoBanco = new JdbcTemplate(dataSource);
    }
    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
}