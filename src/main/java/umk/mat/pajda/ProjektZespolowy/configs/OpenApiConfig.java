package umk.mat.pajda.ProjektZespolowy.configs;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer")
public class OpenApiConfig {
  @Bean
  public GroupedOpenApi customApi() {
    return GroupedOpenApi.builder()
        .group("custom")
        .packagesToScan("umk.mat.pajda.ProjektZespolowy")
        .build();
  }
}
