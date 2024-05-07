//package gogood.gogoodapi.domain.models.config;
//
//import io.r2dbc.spi.ConnectionFactories;
//import io.r2dbc.spi.ConnectionFactory;
//import io.r2dbc.spi.ConnectionFactoryOptions;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
//import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
//
//import static io.r2dbc.spi.ConnectionFactoryOptions.*;
//
//public final class R2dbcConfig extends AbstractR2dbcConfiguration {
//
//    @Override
//    @Bean
//    public @NotNull ConnectionFactory connectionFactory() {
//        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
//                .option(DRIVER, "r2dbc-mysql")
//                .option(PROTOCOL, "mysql")
//                .option(HOST, "191.234.214.229")
//                .option(PORT, 3306)
//                .option(USER, "root")
//                .option(PASSWORD, "Root@123")
//                .option(DATABASE, "GoGood")
//                .build());
//    }
//
//    @Bean
//    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
//        return new R2dbcEntityTemplate(connectionFactory);
//    }
//}