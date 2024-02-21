package umk.mat.pajda.ProjektZespolowy.services.impl;

import java.util.HashMap;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.UserConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;
import umk.mat.pajda.ProjektZespolowy.services.AuthenticationService;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final UserConverter userConverter;

  private final JWTService jwtService;
  private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

  public Boolean register(RegisterDTO registerDTO) {
    try {
      userRepository.save(userConverter.createEntity(registerDTO));
    } catch (Exception e) {
      logger.error("addUser", e);
      return false;
    }
    return true;
  }

  public JWTAuthenticationResponseDTO login(LoginDTO loginDTO) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDTO.getMail(), loginDTO.getPassword()));
    var user =
        userRepository.findByMail(loginDTO.getMail()).orElseThrow(IllegalArgumentException::new);
    var jwt = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

    JWTAuthenticationResponseDTO jwtAuthenticationResponseDTO = new JWTAuthenticationResponseDTO();
    jwtAuthenticationResponseDTO.setToken(jwt);
    jwtAuthenticationResponseDTO.setRefreshToken(refreshToken);
    return jwtAuthenticationResponseDTO;
  }

  public JWTAuthenticationResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
    String userEmail = jwtService.extractUserName(refreshTokenDTO.getToken());
    User user = userRepository.findByMail(userEmail).orElseThrow();
    if (jwtService.isTokenValid(refreshTokenDTO.getToken(), user)) {
      var jwt = jwtService.generateToken(user);
      JWTAuthenticationResponseDTO jwtAuthenticationResponseDTO =
          new JWTAuthenticationResponseDTO();
      jwtAuthenticationResponseDTO.setToken(jwt);
      jwtAuthenticationResponseDTO.setRefreshToken(refreshTokenDTO.getToken());
      return jwtAuthenticationResponseDTO;
    }
    return null;
  }

  public boolean getUser(String mail) {
    try {
      userRepository.findByMail(mail).get();
    } catch (NoSuchElementException e) {
      return false;
    }
    return true;
  }
}
