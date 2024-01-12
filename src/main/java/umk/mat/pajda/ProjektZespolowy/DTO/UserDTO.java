package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umk.mat.pajda.ProjektZespolowy.validators.PatternPassword;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  @NotNull @Positive private Integer id;

  @Size(max = 30)
  @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*$")
  private String name;

  @Size(max = 30)
  @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*+(?:[- ]?[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*)?$")
  private String surname;

  @Size(max = 30)
  @PatternPassword
  private String password;

  @Email private String mail;

  private String location;

  private List<TipDTO> tipDTOList;

  private List<ReviewDTO> reviewDTOList;
}
