package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
  @NotNull
  @Size(min = 1, max = 20)
  private String nick;

  @Email private String mail;

  @NotNull
  @Size(min = 1, max = 1500)
  private String text;
}
