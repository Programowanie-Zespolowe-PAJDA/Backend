package umk.mat.pajda.ProjektZespolowy.services;

import umk.mat.pajda.ProjektZespolowy.DTO.JWTAuthenticationResponseDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.RefreshTokenDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;

public interface AuthenticationService {

  Boolean register(UserDTO userDTO);

  JWTAuthenticationResponseDTO login(UserDTO userDTO);

  JWTAuthenticationResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO);

  UserDTO getUser(String mail);
}
