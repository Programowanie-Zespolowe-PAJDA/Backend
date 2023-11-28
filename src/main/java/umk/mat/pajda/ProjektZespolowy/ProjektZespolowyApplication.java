package umk.mat.pajda.ProjektZespolowy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Kellner app",
		description = "An application that allows the waiter to receive tips electronically and assess his skills." +
				" Backend API Documentation ",
		version = "v0.1"))
public class ProjektZespolowyApplication {
  public static void main(String[] args) {
    SpringApplication.run(ProjektZespolowyApplication.class, args);
  }
}
