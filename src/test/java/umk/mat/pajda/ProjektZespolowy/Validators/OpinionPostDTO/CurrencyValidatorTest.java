package umk.mat.pajda.ProjektZespolowy.Validators.OpinionPostDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;

@ActiveProfiles("tests")
public class CurrencyValidatorTest {
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
  public void shouldSuccessTest() {
    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setCurrency("PLN");
    Set<ConstraintViolation<OpinionPostDTO>> violations =
        validator.validateProperty(opinionPostDTO, "currency");

    Assertions.assertTrue(violations.isEmpty());
  }

  @Test
  public void shouldFailWhenIncorrectCurrencyTest() {
    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setCurrency("BAD");
    Set<ConstraintViolation<OpinionPostDTO>> violations =
        validator.validateProperty(opinionPostDTO, "currency");

    Assertions.assertFalse(violations.isEmpty());
  }
}
