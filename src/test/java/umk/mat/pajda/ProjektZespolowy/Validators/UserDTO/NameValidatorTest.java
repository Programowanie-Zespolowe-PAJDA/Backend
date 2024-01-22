package umk.mat.pajda.ProjektZespolowy.Validators.UserDTO;

import jakarta.validation.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.CreatingEntityGroup;

@ActiveProfiles("tests")
public class NameValidatorTest {

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
  public void shouldFailWhenNoStartsWithUppercaseTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setName("te");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "name", CreatingEntityGroup.class);
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenContainsDigitTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setName("Te1");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "name", CreatingEntityGroup.class);
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenContainsSpecialCharacterTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setName("Te!");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "name", CreatingEntityGroup.class);
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenUppercaseInsideTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setName("TesT");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "name", CreatingEntityGroup.class);
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldSuccessTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setName("Test");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "name", CreatingEntityGroup.class);
    Assertions.assertTrue(violations.isEmpty());
  }
}
