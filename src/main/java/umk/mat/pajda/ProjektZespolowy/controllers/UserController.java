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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.services.UserService;

@RequestMapping("/user")
@RestController
@Tag(
    name = "User Endpoints",
    description = "Controller for handling requests related to add/del/patch/read users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PatchMapping("/editInformations")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "PATCH - modify  \"User\" informations",
      description = "Following endpoint edit User informations")
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
      summary = "PATCH - modify  \"User\" password",
      description = "Following endpoint edit User password")
  public ResponseEntity<String> modPasswordOfUser(
      @AuthenticationPrincipal UserDetails userDetails,
      @Valid @RequestBody UserPatchPasswordDTO userPatchPasswordDTO,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
    }
    if (userService.getUser(userDetails.getUsername()) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed - no user");
    }
    if (!(userPatchPasswordDTO.getPassword().equals(userPatchPasswordDTO.getRetypedPassword()))) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("modifying failed - passwords don't match");
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
      summary = "PATCH - modify  \"User\" email",
      description = "Following endpoint edit User email")
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
    if (userService.patchEmailOfUser(userPatchEmailDTO, userDetails.getUsername())) {
      return ResponseEntity.status(HttpStatus.OK).body("modifying successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed");
    }
  }

  @DeleteMapping
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "DELETE - delete \"Owner\"",
      description = "Following endpoint deletes Owner")
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
      summary = "DELETE - delete \"User\"",
      description = "Following endpoint deletes a User of id")
  public ResponseEntity<String> delUser(@PathVariable int id) {
    if (userService.deleteSelectedUser(id)) {
      return ResponseEntity.status(HttpStatus.OK).body("delete successful");
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("delete failed");
    }
  }

  @GetMapping
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(summary = "GET - get all \"User\"", description = "Following endpoint all Users")
  public ResponseEntity<List<UserGetDTO>> readUser() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
  }

  @GetMapping("/profile")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(summary = "GET - get \"Owner\"", description = "Following endpoint returns a Owner")
  public ResponseEntity<UserGetDTO> readUser(@AuthenticationPrincipal UserDetails userDetails) {
    UserGetDTO returnData = userService.getUser(userDetails.getUsername());
    if (returnData != null) {
      return ResponseEntity.status(HttpStatus.OK).body(returnData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "GET - get \"User\"",
      description = "Following endpoint returns a User of id")
  public ResponseEntity<UserGetDTO> readUser(@PathVariable int id) {
    UserGetDTO returnData = userService.getUser(id);
    if (returnData != null) {
      return ResponseEntity.status(HttpStatus.OK).body(returnData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }
}
