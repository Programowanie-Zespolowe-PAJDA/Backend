package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id @GeneratedValue @NotNull
  private Integer id;

  @Size(min=0, max=10)
  private Integer rating;

  @Size(max=300)
  private String comment;

  @FutureOrPresent
  private LocalDateTime reviewTimeStamp;

  @Size(max=30)
  private String clientName;

  @NotNull
  private String hashRevID;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "id",  nullable = false)
  private User user;
}
