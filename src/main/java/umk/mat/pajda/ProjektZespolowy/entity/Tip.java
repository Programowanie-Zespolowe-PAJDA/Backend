package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tip {

  @Id @NotNull private String id;

  @Pattern(regexp = "^(PLN|EUR|USD|GBP|CHF|DKK|SEK)$")
  private String currency;

  private Integer amount;

  private Integer realAmount;

  private LocalDateTime createdAt;

  private String paidWith;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  private User user;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Review review;

  public String toString2() {
    return "Tip{"
        + "id='"
        + id
        + '\''
        + ", currency='"
        + currency
        + '\''
        + ", amount="
        + amount
        + ", realAmount="
        + realAmount
        + ", paidWith='"
        + paidWith
        + '\''
        + ", user="
        + user.toString2()
        + ", review="
        + review
        + '}';
  }
}
