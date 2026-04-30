package hu.psprog.leaflet.tlql.it.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

/**
 * Additional configuration for integration test context.
 *
 * @author Peter Smith
 */
@Configuration
public class ITContextConfig {

    @Bean
    public JsonMapper JsonMapper() {
        return new JsonMapper();
    }
}
