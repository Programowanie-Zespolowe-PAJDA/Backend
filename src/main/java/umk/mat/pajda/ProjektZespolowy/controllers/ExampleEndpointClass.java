package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(
    name = "Example Endpoint",
    description = "Controller for handling ExampleEndpoint API requests")
public class ExampleEndpointClass {

  @GetMapping("/hello")
  @ResponseBody
  @Operation(summary = "Get hellow", description = "Returns hellow")
  public String sayHello() {
    return "hello";
  }

  @GetMapping("/admin")
  @ResponseBody
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get \"admin\" message",
      description = "Returns admin when you role is ADMIN")
  public String sayAdmin() {
    return "admin";
  }

  @GetMapping("/authenticated")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseBody
  @Operation(
      summary = "Get \"authenticated\" message",
      description = "Returns authenticated when you are authenticated")
  public String sayAuthenticated() {
    return "authenticated";
  }
}
