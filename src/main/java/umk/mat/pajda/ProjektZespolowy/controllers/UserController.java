package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
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

  @PostMapping("/add")
  @Operation(summary = "POST - Add \"new User\"", description = "Following endpoint adds new User")
  public ResponseEntity<String> addNewUser(UserDTO userDTO) {
    if (userService.getUser(userDTO.getName()) != null) {
      return ResponseEntity.status(HttpStatus.FOUND).body("adding failed - already exist");
    }

    if (userService.addUser(userDTO)) {
      return ResponseEntity.status(HttpStatus.CREATED).body("adding successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("adding failed");
    }
  }

  @PatchMapping("/patch")
  @Operation(
      summary = "PATCH - modify \"User\"",
      description = "Following endpoint modifies a User")
  public ResponseEntity<String> modUser(@RequestBody UserDTO userDTO) {
    if (userService.getUser(userDTO.getId()) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed");
    }

    if (userService.patchSelectedUser(userDTO)) {
      return ResponseEntity.status(HttpStatus.OK).body("modifying successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed");
    }
  }

  @DeleteMapping("/del/{id}")
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

  @GetMapping("/get")
  @Operation(summary = "Get - get all \"User\"", description = "Following endpoint returns a User")
  public ResponseEntity<List<UserDTO>> readUser() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
  }

  @GetMapping("/get/{id}")
  @Operation(
      summary = "Get - get \"User\"",
      description = "Following endpoint returns a User of id")
  public ResponseEntity<UserDTO> readUser(@PathVariable int id) {
    UserDTO returnData = userService.getUser(id);
    if (returnData != null) {
      return ResponseEntity.status(HttpStatus.OK).body(returnData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }
}
