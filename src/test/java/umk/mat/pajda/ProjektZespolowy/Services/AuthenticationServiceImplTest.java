package umk.mat.pajda.ProjektZespolowy.Services;

import static org.mockito.ArgumentMatchers.any;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.UserConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthenticationServiceImpl;

public class AuthenticationServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private UserConverter userConverter;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private JWTService jwtService;

  @InjectMocks private AuthenticationServiceImpl authenticationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldSuccessWhenGetUserTest() {
    String mail = "test@test.com";
    User user = new User();
    UserGetDTO userGetDTO = new UserGetDTO();
    user.setMail(mail);
    userGetDTO.setMail(mail);
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
    Mockito.when(userConverter.createDTO(user)).thenReturn(userGetDTO);

    Assertions.assertNotNull(authenticationService.getUser(mail));
  }

  @Test
  public void shouldUserNotFoundWhenGetUserTest() {
    String mail = "test@test.com";
    User user = new User();
    user.setMail(mail);
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.empty());

    Assertions.assertNull(authenticationService.getUser(mail));
  }

  @Test
  public void shouldSuccessWhenUserRegisterTest() {
    String mail = "test@test.com";
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setMail(mail);
    User user = new User();
    user.setMail(mail);
    Mockito.when(userConverter.createEntity(registerDTO)).thenReturn(user);
    Mockito.when(userRepository.save(user)).thenReturn(null);

    Assertions.assertTrue(authenticationService.register(registerDTO));
  }

  @Test
  public void shouldFailWhenUserRegisterAndThrowRuntimeExceptionTest() {
    String mail = "test@test.com";
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setMail(mail);
    User user = new User();
    user.setMail(mail);
    Mockito.when(userConverter.createEntity(registerDTO)).thenReturn(user);
    Mockito.when(userRepository.save(user)).thenThrow(new RuntimeException());

    Assertions.assertFalse(authenticationService.register(registerDTO));
  }

  @Test
  public void shouldSuccessWhenUserLoginTest() {
    String mail = "test@test.com";
    String password = "Testt!123";
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setMail(mail);
    loginDTO.setPassword(password);
    User user = new User();
    user.setMail(mail);
    user.setPassword(password);
    user.setEnabled(true);
    JWTAuthenticationResponseDTO jwtAuthenticationResponseDTO = new JWTAuthenticationResponseDTO();
    jwtAuthenticationResponseDTO.setToken("token");
    jwtAuthenticationResponseDTO.setRefreshToken("refreshToken");
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(userRepository.findByMail(loginDTO.getMail())).thenReturn(Optional.of(user));
    Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    Mockito.when(jwtService.generateToken(user)).thenReturn("token");
    Mockito.when(jwtService.generateRefreshToken(new HashMap<>(), user)).thenReturn("refreshToken");

    Assertions.assertEquals(
        authenticationService.login(loginDTO).getToken(), jwtAuthenticationResponseDTO.getToken());
    Assertions.assertEquals(
        authenticationService.login(loginDTO).getRefreshToken(),
        jwtAuthenticationResponseDTO.getRefreshToken());
  }

  @Test
  public void shouldFailWhenUserLoginAndThrowAuthenticationExceptionTest() {
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setMail("test@test.com");
    loginDTO.setPassword("Testt!123");
    Mockito.when(userRepository.findByMail(loginDTO.getMail())).thenReturn(Optional.of(new User()));
    Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(Mockito.mock(AuthenticationException.class));

    Assertions.assertThrows(
        AuthenticationException.class,
        () -> {
          authenticationService.login(loginDTO);
        });
  }

  @Test
  public void shouldSuccessWhenRefreshTokenTest() throws SignatureException {
    String mail = "test@test.com";
    User user = new User();
    user.setMail(mail);
    JWTAuthenticationResponseDTO jwtAuthenticationResponseDTO = new JWTAuthenticationResponseDTO();
    jwtAuthenticationResponseDTO.setToken("newToken");
    jwtAuthenticationResponseDTO.setRefreshToken("token");
    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
    refreshTokenDTO.setToken("token");
    Mockito.when(jwtService.extractUserName("token")).thenReturn("test@test.com");
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
    Mockito.when(jwtService.isTokenValid("token", user)).thenReturn(true);
    Mockito.when(jwtService.generateToken(user)).thenReturn("newToken");

    Assertions.assertEquals(
        authenticationService.refreshToken(refreshTokenDTO).getRefreshToken(),
        jwtAuthenticationResponseDTO.getRefreshToken());
    Assertions.assertEquals(
        authenticationService.refreshToken(refreshTokenDTO).getToken(),
        jwtAuthenticationResponseDTO.getToken());
  }

  @Test
  public void shouldFailWhenTokenIsNotValidTest() throws SignatureException {
    String mail = "test@test.com";
    User user = new User();
    user.setMail(mail);
    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
    refreshTokenDTO.setToken("token");
    Mockito.when(jwtService.extractUserName("token")).thenReturn("test@test.com");
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
    Mockito.when(jwtService.isTokenValid("token", user)).thenReturn(false);

    Assertions.assertNull(authenticationService.refreshToken(refreshTokenDTO));
  }
}
