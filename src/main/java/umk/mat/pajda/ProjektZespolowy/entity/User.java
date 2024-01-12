package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

  @Id @GeneratedValue @NotNull private Integer id;

  @Size(max = 30)
  private String name;

  @Size(max = 30)
  private String surname;

  @Size(max = 30)
  private String password;

  @Email private String mail;

  private String location;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<Tip> tipList;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<Review> reviewList;
}
