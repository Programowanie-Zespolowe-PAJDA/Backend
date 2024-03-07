package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipPatchDTO {

  @DecimalMin(value = "0.80")
  private BigDecimal amount;

  @Pattern(regexp = "^(CHF|CZK|DKK|EUR|GBP|HUF|NOK|PLN|RON|SEK|USD)$")
  private String currency;

  private String paidWith;

  @NotNull @Positive private Integer userId;
}
