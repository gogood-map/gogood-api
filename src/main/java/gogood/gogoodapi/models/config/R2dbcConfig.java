package gogood.gogoodapi.models.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

public final class R2dbcConfig extends AbstractR2dbcConfiguration {

    @Override
    @Bean
    public @NotNull ConnectionFactory connectionFactory() {
        // Substituir "mysql" por "r2dbc-mysql"
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "r2dbc-mysql")
                .option(PROTOCOL, "mysql") // Este é o protocolo usado pelo MySQL. Pode ser omitido se estiver usando a opção DRIVER correta.
                .option(HOST, "52.226.122.160")
                .option(PORT, 3306)
                .option(USER, "root")
                .option(PASSWORD, "Root@123")
                .option(DATABASE, "GoGood")
                // Removido para usar a construção de URL adequada
                .build());
    }

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }
}