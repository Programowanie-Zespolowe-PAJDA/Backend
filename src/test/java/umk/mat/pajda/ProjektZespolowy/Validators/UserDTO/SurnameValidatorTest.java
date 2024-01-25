package umk.mat.pajda.ProjektZespolowy.Validators.UserDTO;

import jakarta.validation.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.CreatingEntityGroup;

@ActiveProfiles("tests")
public class SurnameValidatorTest {

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
    userDTO.setSurname("te");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenContainsDigitTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("Te1");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenContainsSpecialCharacterTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("Te!");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenHasMoreThan2Parts1Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("Test-Test-Test");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenHasMoreThan2Parts2Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("Test Test Test");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenHasMoreThan2Parts3Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("TestTestTest");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldSuccess1Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("Test");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldSuccess2Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("Test-Test");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldSuccess3Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("TestTest");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldSuccess4Test() {
    UserDTO userDTO = new UserDTO();
    userDTO.setSurname("Test Test");
    Set<ConstraintViolation<UserDTO>> violations =
        validator.validateProperty(userDTO, "surname", CreatingEntityGroup.class);

    Assertions.assertTrue(violations.isEmpty());
  }
}
