package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class ReviewDTO {
  private Integer id;

  @Size(
      max = 10,
      groups = {CreatingEntityGroup.class, EditingEntityGroup.class})
  private Integer rating;

  @Size(
      max = 1500,
      groups = {CreatingEntityGroup.class, EditingEntityGroup.class})
  private String comment;

  @Size(
      max = 30,
      groups = {CreatingEntityGroup.class, EditingEntityGroup.class})
  private String clientName;

  @NotNull(groups = {CreatingEntityGroup.class})
  private String hashRevID;

  @NotNull(groups = {CreatingEntityGroup.class})
  @Positive(groups = {CreatingEntityGroup.class})
  private Integer userID;

  @FutureOrPresent(groups = {EditingEntityGroup.class})
  private LocalDateTime createdAt;
}
