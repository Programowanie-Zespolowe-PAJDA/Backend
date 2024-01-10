package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Review {

  @Id @GeneratedValue @NotNull private Integer id;

  private Integer rating;

  private String comment;

  private LocalDateTime reviewTimeStamp;

  private String clientName;

  private String hashRevID;

  private Integer kellnerID;
}
