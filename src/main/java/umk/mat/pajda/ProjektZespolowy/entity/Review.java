package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id @GeneratedValue @NotNull private Integer id;

  @Size(max = 10)
  private Integer rating;

  @Size(max = 1500)
  private String comment;

  @FutureOrPresent private LocalDateTime reviewTimeStamp;

  @Size(max = 30)
  private String clientName;

  @NotNull private String hashRevID;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;
}
