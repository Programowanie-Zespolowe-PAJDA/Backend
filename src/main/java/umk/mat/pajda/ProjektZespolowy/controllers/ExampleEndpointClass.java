package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(
    name = "ExampleEndpointClass",
    description = "Kontroler do sprawdzenia autoryzacji i autentykacji")
public class ExampleEndpointClass {

  @GetMapping("/hello")
  @ResponseBody
  @Operation(
      summary = "Zwracanie \"hello\"",
      description = "Ten endpoint zwraca \"hello\" dla każdego użytkownika,")
  public String sayHello() {
    return "hello";
  }

  @GetMapping("/admin")
  @ResponseBody
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Zwracanie \"admin\"",
      description = "Ten endpoint zwraca  \"admin\" w przypadku gdy mamy role ADMIN")
  public String sayAdmin() {
    return "admin";
  }

  @GetMapping("/authenticated")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseBody
  @Operation(
      summary = "Zwracanie \"authenticated\"",
      description = "Ten endpoint zwraca \"authenticated\" w przypadku gdy jesteśmy zalogowani")
  public String sayAuthenticated() {
    return "authenticated";
  }
}
