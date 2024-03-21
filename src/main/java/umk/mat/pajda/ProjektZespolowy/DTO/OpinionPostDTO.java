package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpinionPostDTO {
  @Range(min = 0, max = 10)
  private Integer rating;

  @Size(max = 1500)
  private String comment;

  @Size(max = 30)
  private String clientName;

  @NotNull private String hashRevID;

  @NotNull @Positive private Integer userID;

  @Min(value = 80)
  private Integer amount;

  @Pattern(regexp = "^(PLN|EUR|USD|GBP|CHF|DKK|SEK)$")
  private String currency;
}
