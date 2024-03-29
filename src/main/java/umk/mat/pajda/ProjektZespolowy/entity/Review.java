package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id @NotNull private String id;

  @Range(min = 0, max = 10)
  private Integer rating;

  @Size(max = 1500)
  private String comment;

  private LocalDateTime createdAt;

  @Size(max = 30)
  private String clientName;

  @NotNull private String hashRevID;

  private boolean enabled;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;
}
