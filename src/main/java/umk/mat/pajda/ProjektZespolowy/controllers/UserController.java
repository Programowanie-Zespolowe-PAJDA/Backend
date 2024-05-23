package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.services.UserService;

@RequestMapping("/user")
@RestController
@Tag(name = "UserController", description = "Kontroler do obsługiwania użytkownika")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PatchMapping("/editInformations")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Modyfikowanie informacji użytkownika",
      description =
          "Ten endpoint odpowiada za modyfikacje informacji dla zalogowanego użytkownika. Sprawdza takie rzeczy jak: \n"
              + "- walidacje\n"
              + "- czy taki użytkownik istnieje\n\n"
              + "Jeżeli któraś z tych rzeczy zakończy się błędem, "
              + "to nie następuje modyfikacja informacji użytkownika.\n"
              + "W każdym przypadku dostajemy stosowaną informację wraz z odpowiednim statusem.\n")
  public ResponseEntity<String> modInformationsOfUser(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody UserPatchInformationsDTO userPatchInformationsDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
    }
    if (userService.getUser(userDetails.getUsername()) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed - no user");
    }

    if (userService.patchInformationsOfUser(userPatchInformationsDTO, userDetails.getUsername())) {
      return ResponseEntity.status(HttpStatus.OK).body("modifying successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed");
    }
  }

  @PatchMapping("/editPassword")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Modyfikowanie hasła użytkownika",
      description =
          "Ten endpoint odpowiada za modyfikacje hasła dla zalogowanego użytkownika. Sprawdza takie rzeczy jak: \n"
              + "- walidacje\n"
              + "- czy taki użytkownik istnieje\n"
              + "- czy wprowadzone stare hasło jest poprawne\n"
              + "- czy nowe hasło i powtórzone hasło są takie same\n"
              + "- czy stare hasło i nowe hasło są inne\n\n"
              + "Jeżeli któraś z tych rzeczy zakończy się błędem, to nie następuje modyfikacja hasła użytkownika.\n"
              + "W każdym przypadku dostajemy stosowaną informację wraz z odpowiednim statusem.\n")
  public ResponseEntity<String> modPasswordOfUser(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody UserPatchPasswordDTO userPatchPasswordDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
    }
    User user = userService.getUser(userDetails.getUsername());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed - no user");
    }
    if (!BCrypt.checkpw(userPatchPasswordDTO.getOldPassword(), user.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("modifying failed - bad password");
    }
    if (!(userPatchPasswordDTO.getPassword().equals(userPatchPasswordDTO.getRetypedPassword()))) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("modifying failed - passwords don't match");
    }
    if (userPatchPasswordDTO.getOldPassword().equals(userPatchPasswordDTO.getPassword())) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("modifying failed - password are the same");
    }
    if (userService.patchPasswordOfUser(userPatchPasswordDTO, userDetails.getUsername())) {
      return ResponseEntity.status(HttpStatus.OK).body("modifying successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed");
    }
  }

  @PatchMapping("/editEmail")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Modyfikowanie emailu",
      description =
          "Ten endpoint odpowiada za modyfikacje emailu dla zalogowanego użytkownika. Sprawdza takie rzeczy jak: \n"
              + "- walidacje\n"
              + "- czy taki użytkownik istnieje\n"
              + "- czy nowy email i powtórzony email są takie sam\n"
              + "- czy stary email i nowy email są inne\n\n"
              + "Jeżeli któraś z tych rzeczy zakończy się błędem, to nie następuje modyfikacja emailu użytkownika.\n"
              + "W każdym przypadku dostajemy stosowaną informację wraz z odpowiednim statusem.\n")
  public ResponseEntity<String> modEmailOfUser(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody UserPatchEmailDTO userPatchEmailDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
    }
    if (userService.getUser(userDetails.getUsername()) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed - no user");
    }
    if (!userPatchEmailDTO.getMail().equals(userPatchEmailDTO.getRetypedMail())) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("modifying failed - emails aren't the same");
    }
    if (userPatchEmailDTO.getMail().equals(userDetails.getUsername())) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("modifying failed - emails are the same");
    }
    if (userService.patchEmailOfUser(userPatchEmailDTO, userDetails.getUsername())) {
      return ResponseEntity.status(HttpStatus.OK).body("modifying successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed");
    }
  }

  @PatchMapping("/editBankAccountNumber")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Modyfikowanie numeru rachunku bankowego",
      description =
          "Ten endpoint odpowiada za modyfikacje numeru rachunku bankowego dla zalogowanego użytkownika. "
              + "Sprawdza takie rzeczy jak: \n"
              + "- walidacje\n"
              + "- czy taki użytkownik istnieje\n\n"
              + "Jeżeli któraś z tych rzeczy zakończy się błędem, to nie "
              + "następuje modyfikacja numeru rachunku bankowego użytkownika.\n"
              + "W każdym przypadku dostajemy stosowaną informację wraz z odpowiednim statusem.\n")
  public ResponseEntity<String> modBankAccountNumberOfUser(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody UserPatchBankAccountNumberDTO userPatchBankAccountNumberDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
    }
    if (userService.getUser(userDetails.getUsername()) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed - no user");
    }
    if (userService.patchBankAccountNumberOfUser(
        userPatchBankAccountNumberDTO, userDetails.getUsername())) {
      return ResponseEntity.status(HttpStatus.OK).body("modifying successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("modifying failed - wrong BANK ACCOUNT NUMBER [BBAN] ");
    }
  }

  @DeleteMapping
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Usuwanie użytkownika",
      description = "Ten endpoint usuwa konto dla zalogowanego użytkownika.")
  public ResponseEntity<String> delUser(@AuthenticationPrincipal UserDetails userDetails) {
    if (userService.deleteSelectedUser(userDetails.getUsername())) {
      return ResponseEntity.status(HttpStatus.OK).body("delete successful");
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("delete failed");
    }
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Usuwanie danego użytkownika",
      description = "Ten endpoint usuwa konto dla użytkownika o danym id.")
  public ResponseEntity<String> delUser(@PathVariable int id) {
    if (userService.deleteSelectedUser(id)) {
      return ResponseEntity.status(HttpStatus.OK).body("delete successful");
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("delete failed");
    }
  }

  @GetMapping
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Zwracanie użytkowników",
      description = "Ten endpoint zwraca listę wszystkich użytkowników.")
  public ResponseEntity<List<UserGetDTO>> readUser() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
  }

  @GetMapping("/profile")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Zwracanie zalogowanego użytkownika",
      description = "Ten endpoint zwraca zalogowanego użytkownika.")
  public ResponseEntity<UserGetDTO> readUser(@AuthenticationPrincipal UserDetails userDetails) {
    UserGetDTO returnData = userService.getUserDTO(userDetails.getUsername());
    if (returnData != null) {
      return ResponseEntity.status(HttpStatus.OK).body(returnData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Zwracanie danego użytkownika",
      description = "Ten endpoint zwraca użytkownika o danym id.")
  public ResponseEntity<UserGetDTO> readUser(@PathVariable int id) {
    UserGetDTO returnData = userService.getUser(id);
    if (returnData != null) {
      return ResponseEntity.status(HttpStatus.OK).body(returnData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }
}
