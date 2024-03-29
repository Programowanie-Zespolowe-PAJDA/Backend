package umk.mat.pajda.ProjektZespolowy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
    info =
        @Info(
            title = "Kellner app",
            description =
                "An application that allows the waiter to receive tips electronically "
                    + "and assess his skills. Backend API Documentation ",
            version = "v0.5"))
public class ProjektZespolowyApplication {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  public static void main(String[] args) {
    SpringApplication.run(ProjektZespolowyApplication.class, args);
  }
}
