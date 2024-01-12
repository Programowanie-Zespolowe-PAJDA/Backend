package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipDTO {

  @NotNull @Positive private Integer id;

  private String currency;

  @NotNull private Float amount;

  @FutureOrPresent private LocalDateTime paymentTime;

  private String paidWith;

  @NotNull private UserDTO user;
}
