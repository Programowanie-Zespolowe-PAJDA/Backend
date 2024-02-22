package umk.mat.pajda.ProjektZespolowy.services.impl;

import java.util.HashMap;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.Token;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.NotEnabledException;
import umk.mat.pajda.ProjektZespolowy.misc.UserConverter;
import umk.mat.pajda.ProjektZespolowy.repository.TokenRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;
import umk.mat.pajda.ProjektZespolowy.services.AuthenticationService;
import umk.mat.pajda.ProjektZespolowy.services.EmailService;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.TokenService;

@Service
@Profile("!tests")
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final UserConverter userConverter;

  private final JWTService jwtService;
  private final TokenRepository tokenRepository;
  private final EmailService emailService;
  private final TokenService tokenService;
  private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

  @Value("${spring.profiles.active}")
  private String activeProfile;

  public AuthenticationServiceImpl(
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      UserConverter userConverter,
      JWTService jwtService,
      TokenRepository tokenRepository,
      EmailService emailService,
      TokenService tokenService) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.userConverter = userConverter;
    this.jwtService = jwtService;
    this.tokenRepository = tokenRepository;
    this.emailService = emailService;
    this.tokenService = tokenService;
  }

  public Boolean register(RegisterDTO registerDTO) {
    try {
      User user = userRepository.save(userConverter.createEntity(registerDTO));
      if ("prod".equals(activeProfile)) {
        Token token = tokenRepository.save(tokenService.createToken(user));
        emailService.send(
            user,
            "Verify your email",
            "Thanks for creating account. \n Please click the following link to activate your account.\n"
                + "https://enapiwek-api.onrender.com/confirm?token="
                + token.getToken());
      }
    } catch (Exception e) {
      logger.error("addUser", e);
      return false;
    }
    return true;
  }

  public JWTAuthenticationResponseDTO login(LoginDTO loginDTO) throws NotEnabledException {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDTO.getMail(), loginDTO.getPassword()));
    var user =
        userRepository.findByMail(loginDTO.getMail()).orElseThrow(IllegalArgumentException::new);
    if (!user.isEnabled()) {
      throw new NotEnabledException("User is not enabled");
    }
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

  public User getUser(String mail) {
    User user = null;
    try {
      user = userRepository.findByMail(mail).get();
    } catch (NoSuchElementException e) {
      return null;
    }
    return user;
  }
}
