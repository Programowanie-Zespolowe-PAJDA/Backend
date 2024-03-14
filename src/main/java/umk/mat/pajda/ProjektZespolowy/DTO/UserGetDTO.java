package umk.mat.pajda.ProjektZespolowy.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserGetDTO {
  private int id;
  private String name;
  private String surname;
  private String mail;
  private String location;
  private String bankAccountNumber;
}
