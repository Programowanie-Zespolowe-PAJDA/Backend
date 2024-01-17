package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  @NotNull @Positive private Integer id;

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

  private List<TipDTO> tipDTOList;

  private List<ReviewDTO> reviewDTOList;
}
