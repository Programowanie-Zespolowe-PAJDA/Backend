package umk.mat.pajda.ProjektZespolowy.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TipMonthDTO {
  private Long amount;
  private String month;
  private String year;

  @JsonIgnore
  private final List<String> months =
      List.of(
          "STYCZEŃ",
          "LUTY",
          "MARZEC",
          "KWIECIEŃ",
          "MAJ",
          "CZERWIEC",
          "LIPIEC",
          "SIERPIEŃ",
          "WRZESIEŃ",
          "PAŹDZIERNIK",
          "LISTOPAD",
          "GRUDZIEŃ");

  public TipMonthDTO(Long amount, Integer month, Integer year) {
    this.amount = amount;
    this.month = months.get(month - 1);
    this.year = String.valueOf(year);
  }
}
