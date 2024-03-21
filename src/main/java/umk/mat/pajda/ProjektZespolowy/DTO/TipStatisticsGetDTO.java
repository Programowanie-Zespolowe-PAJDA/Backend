package umk.mat.pajda.ProjektZespolowy.DTO;

import java.util.List;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TipStatisticsGetDTO {
  private Integer numberOfTips;
  private Integer minTipAmount;
  private Integer maxTipAmount;
  private Double avgTipAmount;
  private String currency;

  private List<TipMonthDTO> sumTipValueForEveryMonth;
  private Double avgRatingOfUser;
}
