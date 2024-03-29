package umk.mat.pajda.ProjektZespolowy.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipGetDTO {
  private String id;
  private String currency;
  private Integer amount;
  private String paidWith;
  private Integer userId;
  private LocalDateTime createdAt;
}
