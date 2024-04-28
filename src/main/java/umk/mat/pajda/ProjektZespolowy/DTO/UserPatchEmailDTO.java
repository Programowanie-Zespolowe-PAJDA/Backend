package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchEmailDTO {
  @Email private String mail;

  @Email private String retypedMail;
}
