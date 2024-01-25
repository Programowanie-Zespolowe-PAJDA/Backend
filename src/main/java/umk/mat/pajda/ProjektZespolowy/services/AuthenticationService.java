package umk.mat.pajda.ProjektZespolowy.services;

import umk.mat.pajda.ProjektZespolowy.DTO.*;

public interface AuthenticationService {

  Boolean register(RegisterDTO registerDTO);

  JWTAuthenticationResponseDTO login(LoginDTO loginDTO);

  JWTAuthenticationResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO);

  boolean getUser(String mail);
}
