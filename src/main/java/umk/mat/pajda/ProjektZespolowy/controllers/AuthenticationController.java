package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umk.mat.pajda.ProjektZespolowy.DTO.JWTAuthenticationResponseDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.RefreshTokenDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.services.AuthenticationService;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.CreatingEntityGroup;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication Endpoints", description = "Controller for login/register/refresh")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  @Operation(summary = "POST - Add \"new User\"", description = "Following endpoint adds new User")
  public ResponseEntity<String> register(
      @Validated(CreatingEntityGroup.class) @RequestBody UserDTO userDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("Validation failed: " + bindingResult.getAllErrors());
    }
    if (!(userDTO.getPassword().equals(userDTO.getRetypedPassword()))) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("adding failed - passwords don't match");
    }
    if (authenticationService.getUser(userDTO.getMail()) != null) {
      return ResponseEntity.status(HttpStatus.FOUND).body("adding failed - already exist");
    }
    if (authenticationService.register(userDTO)) {
      return ResponseEntity.status(HttpStatus.CREATED).body("adding successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("adding failed");
    }
  }

  @PostMapping("/login")
  @Operation(
      summary = "POST - get JWT token",
      description = "Following endpoint return JWT Token By User details")
  public ResponseEntity<JWTAuthenticationResponseDTO> login(@RequestBody UserDTO userDTO) {
    return ResponseEntity.ok(authenticationService.login(userDTO));
  }

  @PostMapping("/refresh")
  @Operation(
      summary = "POST - get JWT token",
      description = "Following endpoint return JWT Token by refresh Token")
  public ResponseEntity<JWTAuthenticationResponseDTO> refresh(
      @RequestBody RefreshTokenDTO refreshTokenDTO) {
    return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenDTO));
  }
}
