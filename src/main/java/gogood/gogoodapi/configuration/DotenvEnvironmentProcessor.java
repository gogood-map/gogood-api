package gogood.gogoodapi.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvEnvironmentProcessor implements EnvironmentPostProcessor {
    private final Dotenv dotenv = Dotenv.load();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> envVariables = new HashMap<>();
        dotenv.entries().forEach(entry -> envVariables.put(entry.getKey(), entry.getValue()));

        PropertySource<Map<String, Object>> propertySource = new MapPropertySource("dotenvProperties", envVariables);
        MutablePropertySources sources = environment.getPropertySources();
        sources.addFirst(propertySource);
    }
}