package umk.mat.pajda.ProjektZespolowy.DTO;

import java.time.LocalDateTime;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TipGetDTO {
  private Integer id;
  private String currency;
  private Float amount;
  private String paidWith;
  private Integer userId;
  private LocalDateTime createdAt;
}
