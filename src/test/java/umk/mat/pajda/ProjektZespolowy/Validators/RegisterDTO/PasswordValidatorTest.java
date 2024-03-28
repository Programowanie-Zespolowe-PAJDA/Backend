package umk.mat.pajda.ProjektZespolowy.Validators.RegisterDTO;

import jakarta.validation.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.RegisterDTO;

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
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setPassword("testt1!t");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "password");
    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoLowercaseTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setPassword("BBBBB1!B");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "password");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoDigitTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setPassword("testtt!B");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "password");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenNoSpecialCharacterTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setPassword("testtt1B");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "password");

    Assertions.assertFalse(violations.isEmpty());
  }

  @Test
  public void shouldSuccessTest() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setPassword("testt!1B");
    Set<ConstraintViolation<RegisterDTO>> violations =
        validator.validateProperty(registerDTO, "password");

    Assertions.assertTrue(violations.isEmpty());
  }
}
