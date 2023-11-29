package umk.mat.pajda.ProjektZespolowy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "KellnerApp", description = "Doc"))
public class ProjektZespolowyApplication {
  public static void main(String[] args) {
    SpringApplication.run(ProjektZespolowyApplication.class, args);
  }
}
