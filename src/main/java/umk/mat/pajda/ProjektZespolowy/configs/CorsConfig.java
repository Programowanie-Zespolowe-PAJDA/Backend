package umk.mat.pajda.ProjektZespolowy.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("!tests")
public class CorsConfig implements WebMvcConfigurer {
  @Value("${cors-allowed-origin}")
  private String origin;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins(origin, "https://secure.snd.payu.com/**")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
