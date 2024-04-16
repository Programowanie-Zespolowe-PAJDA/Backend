package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyGetDTO {

  @Pattern(regexp = "^(PLN|EUR|USD|GBP|CHF|DKK|SEK)$")
  private String currency;
}
