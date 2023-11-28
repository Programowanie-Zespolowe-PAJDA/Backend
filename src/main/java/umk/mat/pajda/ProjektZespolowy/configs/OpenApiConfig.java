package umk.mat.pajda.ProjektZespolowy.configs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {
    @Bean
    public GroupedOpenApi customApi() {
        return GroupedOpenApi.builder()
                .group("custom")
                .packagesToScan("umk.mat.pajda.ProjektZespolowy")
                .build();
    }
}
