package umk.mat.pajda.ProjektZespolowy.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("!tests")
public class CorsConfig implements WebMvcConfigurer {
  private final String origin = System.getenv("CORS_ALLOWED_ORIGIN");

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    System.out.println("CorsConfig.addCorsMappings !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    System.out.println(origin);
    registry
        .addMapping("/**")
        .allowedOrigins(origin, "https://secure.snd.payu.com/**")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
