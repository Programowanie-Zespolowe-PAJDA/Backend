package umk.mat.pajda.ProjektZespolowy.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpinionGetDTO {
  private Integer amount;
  private String currency;
  private String comment;
  private String clientName;
}