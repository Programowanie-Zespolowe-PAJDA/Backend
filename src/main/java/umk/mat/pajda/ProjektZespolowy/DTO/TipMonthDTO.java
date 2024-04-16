package umk.mat.pajda.ProjektZespolowy.DTO;

import java.time.Month;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TipMonthDTO {
  private Long amount;
  private String month;
  private String year;

  public TipMonthDTO(Long amount, Integer month, Integer year) {
    this.amount = amount;
    this.month = Month.of(month).toString();
    this.year = String.valueOf(year);
  }
}
