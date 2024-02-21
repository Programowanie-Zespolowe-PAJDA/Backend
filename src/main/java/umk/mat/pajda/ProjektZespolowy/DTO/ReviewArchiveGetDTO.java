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
public class ReviewArchiveGetDTO {
  private int id;
  private Integer rating;
  private String comment;
  private String clientName;
  private LocalDateTime createdAt;
  private Integer userID;
}
