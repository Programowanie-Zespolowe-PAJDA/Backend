package umk.mat.pajda.ProjektZespolowy.Validators.UserDTO;

import jakarta.validation.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.RegisterDTO;

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
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("te");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenContainsDigitTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("Te1");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenContainsSpecialCharacterTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("Te!");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenHasMoreThan2Parts1Test() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("Test-Test-Test");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenHasMoreThan2Parts2Test() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("Test Test Test");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenHasMoreThan2Parts3Test() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("TestTestTest");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldSuccess1Test() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("Test");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldSuccess2Test() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("Test-Test");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldSuccess3Test() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("TestTest");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldSuccess4Test() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setSurname("Test Test");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "surname");

    Assertions.assertTrue(violations.isEmpty());
  }
}
