package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipPatchDTO {

  @Min(value = 80)
  private Integer amount;

  @Pattern(regexp = "^(CHF|CZK|DKK|EUR|GBP|HUF|NOK|PLN|RON|SEK|USD)$")
  private String currency;

  private String paidWith;

  @NotNull @Positive private Integer userId;
}
