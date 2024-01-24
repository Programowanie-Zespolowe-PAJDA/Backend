package umk.mat.pajda.ProjektZespolowy.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthenticationResponseDTO {
  private String token;
  private String refreshToken;
}
