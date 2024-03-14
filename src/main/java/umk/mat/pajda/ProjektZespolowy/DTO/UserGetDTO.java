package umk.mat.pajda.ProjektZespolowy.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserGetDTO {
  private int id;
  private String name;
  private String surname;
  private String mail;
  private String location;
  private String bankAccountNumber;
}
