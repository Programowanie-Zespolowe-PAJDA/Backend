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
public class ReviewDTO {

  private Integer id;

  private Integer rating;

  private String comment;

  private LocalDateTime reviewTimeStamp;

  private String clientName;

  private String hashRevID;

  private Integer kellnerID;
}
