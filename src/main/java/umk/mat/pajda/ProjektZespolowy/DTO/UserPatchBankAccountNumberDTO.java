package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchBankAccountNumberDTO {

  @Pattern(regexp = "^[0-9]{26}$")
  private String bankAccountNumber;
}
