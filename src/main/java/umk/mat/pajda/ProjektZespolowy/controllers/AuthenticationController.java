package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.Token;
import umk.mat.pajda.ProjektZespolowy.services.AuthenticationService;
import umk.mat.pajda.ProjektZespolowy.services.TokenService;
import umk.mat.pajda.ProjektZespolowy.services.UserService;

@RestController
@Tag(name = "Authentication Endpoints", description = "Controller for login/register/refresh")
public class AuthenticationController {

  @Autowired(required = false)
  private AuthenticationService authenticationService;

  @Autowired(required = false)
  private TokenService tokenService;

  private final UserService userService;

  public AuthenticationController(UserService userService) {
    this.userService = userService;
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
    } catch (DisabledException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("login failed - account is not confirm");
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("login failed - login or password incorrect");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login failed");
    }
  }

  @PostMapping("/refresh")
  @Operation(
      summary = "POST - get JWT token",
      description = "Following endpoint return JWT Token by refresh Token")
  public ResponseEntity<?> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {
    try {
      return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenDTO));
    } catch (SignatureException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh failed - bad token");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh failed");
    }
  }

  @GetMapping("/confirm")
  @Operation(
      summary = "GET - confirm account",
      description = "Following endpoint confirm account by verification Token")
  @Profile("prod")
  public RedirectView confirm(@RequestParam String token) {
    String redirectURL = "https://enapiwek.onrender.com/auth";
    Token confirmToken = tokenService.getToken(token);
    if (confirmToken == null) {
      return new RedirectView(redirectURL + "?isEmailConfirmed=false");
    }
    if (tokenService.isExpired(confirmToken)) {
      return new RedirectView(redirectURL + "?isEmailConfirmed=false");
    }
    if (confirmToken.getNewEmail() == null) {
      if (tokenService.confirm(confirmToken)) {
        return new RedirectView(redirectURL + "?isEmailConfirmed=true");
      } else {
        return new RedirectView(redirectURL + "?isEmailConfirmed=false");
      }
    } else {
      if (userService.setEmail(confirmToken)) {
        return new RedirectView(redirectURL + "?isEmailConfirmed=true");
      } else {
        return new RedirectView(redirectURL + "?isEmailConfirmed=false");
      }
    }
  }
}
