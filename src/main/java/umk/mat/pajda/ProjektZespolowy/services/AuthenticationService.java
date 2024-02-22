package umk.mat.pajda.ProjektZespolowy.services;

import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.NotEnabledException;

public interface AuthenticationService {

  Boolean register(RegisterDTO registerDTO);

  JWTAuthenticationResponseDTO login(LoginDTO loginDTO) throws NotEnabledException;

  JWTAuthenticationResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO);

  User getUser(String mail);
}
