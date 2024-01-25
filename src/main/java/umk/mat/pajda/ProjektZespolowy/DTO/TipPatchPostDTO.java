package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipPatchPostDTO {
  private String currency;

  @NotNull private Float amount;

  private String paidWith;

  @NotNull @Positive private Integer userId;
}
