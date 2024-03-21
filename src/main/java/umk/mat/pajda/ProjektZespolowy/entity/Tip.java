package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Tip {

  @Id @NotNull private String id;

  @Pattern(regexp = "^(PLN|EUR|USD|GBP|CHF|DKK|SEK)$")
  private String currency;

  @Min(value = 80)
  private Integer amount;

  @FutureOrPresent private LocalDateTime createdAt;

  private String paidWith;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;
}
