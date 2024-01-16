package umk.mat.pajda.ProjektZespolowy;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.validators.PatternPasswordValidator;

@ActiveProfiles("tests")
public class PatternPasswordValidatorTest {

  private PatternPasswordValidator patternPasswordValidator;

  @Mock private ConstraintValidatorContext mock;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    patternPasswordValidator = new PatternPasswordValidator();
  }

  @Test
  public void noUppercaseTest() {
    Assertions.assertFalse(patternPasswordValidator.isValid("n1!", mock));
  }

  @Test
  public void noLowercaseTest() {
    Assertions.assertFalse(patternPasswordValidator.isValid("N1!", mock));
  }

  @Test
  public void noDigitTest() {
    Assertions.assertFalse(patternPasswordValidator.isValid("Nn!", mock));
  }

  @Test
  public void noSpecialCharacterTest() {
    Assertions.assertFalse(patternPasswordValidator.isValid("Nn1", mock));
  }

  @Test
  public void succesPatternTest() {
    Assertions.assertTrue(patternPasswordValidator.isValid("Nn1!", mock));
  }
}
