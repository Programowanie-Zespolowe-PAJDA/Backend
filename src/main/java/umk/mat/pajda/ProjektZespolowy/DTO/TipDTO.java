package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.CreatingEntityGroup;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.EditingEntityGroup;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipDTO {
  private Integer id;

  private String currency;

  @NotNull(groups = {CreatingEntityGroup.class, EditingEntityGroup.class})
  private Float amount;

  private String paidWith;

  @NotNull(groups = CreatingEntityGroup.class)
  @Positive(groups = CreatingEntityGroup.class)
  private Integer userId;

  @FutureOrPresent(groups = EditingEntityGroup.class)
  private LocalDateTime createdAt;
}
