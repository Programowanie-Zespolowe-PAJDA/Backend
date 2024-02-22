package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.Token;
import umk.mat.pajda.ProjektZespolowy.misc.NotEnabledException;
import umk.mat.pajda.ProjektZespolowy.services.AuthenticationService;
import umk.mat.pajda.ProjektZespolowy.services.TokenService;

@RestController
@Profile("!tests")
@Tag(name = "Authentication Endpoints", description = "Controller for login/register/refresh")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  private final TokenService tokenService;

  public AuthenticationController(
      AuthenticationService authenticationService, TokenService tokenService) {
    this.authenticationService = authenticationService;
    this.tokenService = tokenService;
  }

  @PostMapping("/register")
  @Operation(summary = "POST - Add \"new User\"", description = "Following endpoint adds new User")
  public ResponseEntity<String> register(
      @Valid @RequestBody RegisterDTO registerDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("Validation failed: " + bindingResult.getAllErrors());
    }
    if (!(registerDTO.getPassword().equals(registerDTO.getRetypedPassword()))) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("adding failed - passwords don't match");
    }
    if (authenticationService.getUser(registerDTO.getMail()) != null) {
      return ResponseEntity.status(HttpStatus.FOUND).body("adding failed - already exist");
    }
    if (!authenticationService.register(registerDTO)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("adding failed");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body("adding successful");
  }

  @PostMapping("/login")
  @Operation(
      summary = "POST - get JWT token",
      description = "Following endpoint return JWT Token By User details")
  public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
    try {
      return ResponseEntity.ok(authenticationService.login(loginDTO));
    } catch (NotEnabledException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("login failed - account is not confirm");
    }
  }

  @PostMapping("/refresh")
  @Operation(
      summary = "POST - get JWT token",
      description = "Following endpoint return JWT Token by refresh Token")
  public ResponseEntity<JWTAuthenticationResponseDTO> refresh(
      @RequestBody RefreshTokenDTO refreshTokenDTO) {
    return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenDTO));
  }

  @RequestMapping(
      value = "/confirm",
      method = {RequestMethod.GET, RequestMethod.POST})
  @Operation(
      summary = "confirm account",
      description = "Following endpoint confirm account by verification Token")
  public ResponseEntity<String> confirm(@RequestParam String token) {
    Token confirmToken = tokenService.getToken(token);
    if (confirmToken == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("confirming failed - token not found");
    }
    if (tokenService.isExpired(confirmToken)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("confirming failed - token is expired");
    }
    if (tokenService.confirm(confirmToken)) {
      return ResponseEntity.status(HttpStatus.OK).body("confirming success");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("confirming failed");
    }
  }
}
