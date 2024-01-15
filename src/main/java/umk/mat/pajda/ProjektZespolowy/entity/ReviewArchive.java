package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewArchive {

  @Id @GeneratedValue @NotNull private Integer id;

  @Size(min = 0, max = 10)
  private Integer rating;

  @Size(max = 1500)
  private String comment;

  @PastOrPresent private LocalDateTime reviewTimeStamp;

  @Size(max = 30)
  private String clientName;

  @NotNull private String hashRevID;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
