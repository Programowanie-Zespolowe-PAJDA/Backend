package umk.mat.pajda.ProjektZespolowy.DTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

  @NotNull
  @Positive
  private Integer id;

  @Size(min=0, max=10)
  private Integer rating;

  @Size(max=300)
  private String comment;

  @FutureOrPresent
  private LocalDateTime reviewTimeStamp;

  @Size(max=30)
  private String clientName;

  @NotNull
  private String hashRevID;

  @NotNull
  private UserDTO user;
}
