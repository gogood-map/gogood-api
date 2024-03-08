package gogood.gogoodapi.Models.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcConfig {
    private JdbcTemplate conexaoDoBanco;

    public JdbcConfig() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://viaduct.proxy.rlwy.net:44905/GoGood");
        dataSource.setUsername("root");
        dataSource.setPassword("dEED163g3bDBfeGeHaHD1EGGha6bC46g");
        conexaoDoBanco = new JdbcTemplate(dataSource);
    }
    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
}