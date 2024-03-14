package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
  @Size(min = 2, max = 30)
  @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*$")
  private String name;

  @Size(min = 2, max = 30)
  @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*+(?:[- ]?[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*)?$")
  private String surname;

  @Size(min = 8, max = 30)
  @Pattern(regexp = "^(?=.*[a-ząćęłńóśźż])(?=.*[A-ZĄĆĘŁŃÓŚŹŻ])(?=.*\\d)(?=.*[!@#$%^&*]).*$")
  private String password;

  @Size(min = 8, max = 30)
  @Pattern(regexp = "^(?=.*[a-ząćęłńóśźż])(?=.*[A-ZĄĆĘŁŃÓŚŹŻ])(?=.*\\d)(?=.*[!@#$%^&*]).*$")
  private String retypedPassword;

  @Email private String mail;

  private String location;

  @Pattern(regexp = "^[0-9]{26}$")
  private String bankAccountNumber;
}
