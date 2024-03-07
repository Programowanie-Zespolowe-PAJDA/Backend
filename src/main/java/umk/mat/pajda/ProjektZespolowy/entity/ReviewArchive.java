package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewArchive {

  @Id @NotNull private String id;

  @Range(min = 0, max = 10)
  private Integer rating;

  @Size(max = 1500)
  private String comment;

  @PastOrPresent private LocalDateTime createdAt;

  @Size(max = 30)
  private String clientName;

  @NotNull private String hashRevID;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public String toString2() {
    return "ReviewArchive{"
        + "id="
        + id
        + ", rating="
        + rating
        + ", comment='"
        + comment
        + '\''
        + ", clientName='"
        + clientName
        + '\''
        + ", hashRevID='"
        + hashRevID
        + '\''
        + ", user="
        + user.toString2()
        + '}';
  }
}
