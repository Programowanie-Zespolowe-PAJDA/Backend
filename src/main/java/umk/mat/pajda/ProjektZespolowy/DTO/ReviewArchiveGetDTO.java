package umk.mat.pajda.ProjektZespolowy.DTO;

import java.time.LocalDateTime;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewArchiveGetDTO {
  private int id;
  private Integer rating;
  private String comment;
  private String clientName;
  private LocalDateTime createdAt;
  private Integer userID;
}
