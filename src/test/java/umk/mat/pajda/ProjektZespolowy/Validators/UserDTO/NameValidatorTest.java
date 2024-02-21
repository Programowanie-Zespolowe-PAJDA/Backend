package umk.mat.pajda.ProjektZespolowy.Validators.UserDTO;

import jakarta.validation.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.RegisterDTO;

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
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setName("te");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "name");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenContainsDigitTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setName("Te1");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "name");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenContainsSpecialCharacterTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setName("Te!");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "name");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenUppercaseInsideTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setName("TesT");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "name");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldSuccessTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setName("Test");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "name");

    Assertions.assertTrue(violations.isEmpty());
  }
}
