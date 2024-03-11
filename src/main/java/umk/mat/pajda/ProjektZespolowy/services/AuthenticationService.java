package umk.mat.pajda.ProjektZespolowy.services;

import java.security.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.User;

public interface AuthenticationService {

  Boolean register(RegisterDTO registerDTO);

  JWTAuthenticationResponseDTO login(LoginDTO loginDTO)
      throws DisabledException, BadCredentialsException, IllegalArgumentException;

  JWTAuthenticationResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO)
      throws SignatureException, IllegalArgumentException;

  User getUser(String mail);
}
