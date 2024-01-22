package umk.mat.pajda.ProjektZespolowy.Validators.UserDTO;

import jakarta.validation.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.CreatingEntityGroup;

@ActiveProfiles("tests")
public class PasswordValidatorTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  @BeforeAll
  public static void createValidator() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  public static void close() {
    validatorFactory.close();
  }

  @Test
  public void shouldFailWhenNoUppercaseTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testt1!t");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "password", CreatingEntityGroup.class);
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoLowercaseTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("BBBBB1!B");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "password", CreatingEntityGroup.class);
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoDigitTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testtt!B");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "password", CreatingEntityGroup.class);
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoSpecialCharacterTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testtt1B");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "password", CreatingEntityGroup.class);
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldSuccessTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testt!1B");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "password", CreatingEntityGroup.class);
    Assertions.assertTrue(violations.isEmpty());
  }
}
