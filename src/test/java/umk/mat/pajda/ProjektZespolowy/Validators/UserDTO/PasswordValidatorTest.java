package umk.mat.pajda.ProjektZespolowy.Validators.UserDTO;

import jakarta.validation.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;

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
    Set<ConstraintViolation<UserDTO>> violations = validator.validateProperty(userDTO, "password");
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoLowercaseTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("BBBBB1!B");
    Set<ConstraintViolation<UserDTO>> violations = validator.validateProperty(userDTO, "password");
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoDigitTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testtt!B");
    Set<ConstraintViolation<UserDTO>> violations = validator.validateProperty(userDTO, "password");
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoSpecialCharacterTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testtt1B");
    Set<ConstraintViolation<UserDTO>> violations = validator.validateProperty(userDTO, "password");
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldSuccessWhenPatternAndLengthAreCorrectTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testt!1B");
    Set<ConstraintViolation<UserDTO>> violations = validator.validateProperty(userDTO, "password");
    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenLengthIsLessThan8Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testB!1");
    Set<ConstraintViolation<UserDTO>> violations = validator.validateProperty(userDTO, "password");
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenLengthIsLongerThan30Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setPassword("testB!1aaaaaaaaaaaaaaaaaaaaaaaa");
    Set<ConstraintViolation<UserDTO>> violations = validator.validateProperty(userDTO, "password");
    Assertions.assertFalse(violations.isEmpty());
  }
}
