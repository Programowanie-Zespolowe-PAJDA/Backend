package umk.mat.pajda.ProjektZespolowy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

  @Id @GeneratedValue private Integer id;

  @Size(min = 2, max = 30)
  @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*$")
  private String name;

  @Size(min = 2, max = 30)
  @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*+(?:[- ]?[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*)?$")
  private String surname;

  @Size(min = 8, max = 30)
  @Pattern(regexp = "^(?=.*[a-ząćęłńóśźż])(?=.*[A-ZĄĆĘŁŃÓŚŹŻ])(?=.*\\d)(?=.*[!@#$%^&*]).*$")
  private String password;

  @Email private String mail;

  private String location;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<Tip> tipList;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<Review> reviewList;
}
