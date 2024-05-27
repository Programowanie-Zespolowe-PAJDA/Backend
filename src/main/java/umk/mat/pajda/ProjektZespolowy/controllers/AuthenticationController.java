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
@Tag(
    name = "AuthenticationController",
    description = "Kontroler dla logowania, rejestracji, odnowienia tokenu i potwierdzania")
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
  @Operation(
      summary = "Tworzenie nowego użytkownika",
      description =
          "Ten endpoint odpowiada za rejestracje użytkownika. Sprawdza takie rzeczy jak: \n"
              + "- walidacja\n"
              + "- czy nowe hasło oraz powtórzone hasło są takie same\n"
              + "- czy istnieje już taki użytkownik\n\n"
              + "Jeżeli któraś z tych rzeczy zakończy się błędem, to użytkownik nie jest dodawany do bazy danych.\n"
              + "W każdym przypadku dostajemy stosowaną informację wraz z odpowiednim statusem.")
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
      summary = "Logowanie użytkownika",
      description =
          "Ten endpoint odpowiada za logowanie użytkownika, zwracając token JWT na podstawie wysłanych danych. "
              + "Sprawdza takie rzeczy jak: \n"
              + "- czy konto zostało zweryfikowane\n"
              + "- czy wprowadzone dane są poprawne\n\n"
              + "Jeżeli któraś z tych rzeczy zakończy się błędem, to token JWT nie zostaje zwrócony.\n"
              + "W każdym przypadku dostajemy stosowaną informację wraz z odpowiednim statusem.\n"
              + "W przypadku sukcesu dostajemy także refresh token, który umożliwa nam szybsze uzyskanie"
              + " tokenu JWT w pózniejszym czasie.")
  public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
    try {
      return ResponseEntity.ok(authenticationService.login(loginDTO));
    } catch (DisabledException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Email nie został jeszcze potwierdzony!");
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email lub hasło są niepoprawne");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logowanie nieudane");
    }
  }

  @PostMapping("/refresh")
  @Operation(
      summary = "Odnawianie tokenu JWT",
      description =
          "Ten endpoint odpowiada za generowanie nowego tokenu JWT na podstawie refresh tokenu."
              + "Jeżeli jest prawidłowym tokenem to wysyła nowy token JWT.\n"
              + "W każdym przypadku dostajemy stosowaną informację wraz z odpowiednim statusem.\n")
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
      summary = "Potwierdzanie konta lub nowego emailu",
      description =
          "Ten endpoint odpowiada za potwierdzanie konta lub potwierdzanie zmiany emailu."
              + " Sprawdza takie rzeczy jak: \n"
              + "- czy wartość tokenu w parametrze nie jest nullem\n"
              + "- czy token nie jest wygaśnięty\n\n"
              + "Jeżeli któraś z tych rzeczy zakończy się błędem, to nie jest ukończone potwierdzanie. \n"
              + "W każdym przypadku zostajemy przekierowywani na stronę z odpowiednim komunikatem.")
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
