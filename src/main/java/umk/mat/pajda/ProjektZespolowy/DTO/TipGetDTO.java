package umk.mat.pajda.ProjektZespolowy.DTO;

import java.time.LocalDateTime;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TipGetDTO {
  private String id;
  private String currency;
  private Integer amount;
  private String paidWith;
  private Integer userId;
  private LocalDateTime createdAt;
}
