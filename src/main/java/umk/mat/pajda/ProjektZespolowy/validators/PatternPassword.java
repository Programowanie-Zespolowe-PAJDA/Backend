package umk.mat.pajda.ProjektZespolowy.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PatternPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PatternPassword {
  String message() default
      "Password should contain at least 1 uppercase, 1 lowercase, 1 special character and 1 digit";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
