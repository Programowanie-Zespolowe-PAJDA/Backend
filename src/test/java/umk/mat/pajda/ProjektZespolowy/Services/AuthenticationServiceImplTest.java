package umk.mat.pajda.ProjektZespolowy.Services;

import static org.mockito.ArgumentMatchers.any;

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
import umk.mat.pajda.ProjektZespolowy.DTO.RefreshTokenDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
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
  public void shouldSuccesWhenGetUserTest() {
    String mail = "test@test.com";
    User user = new User();
    UserDTO userDTO = new UserDTO();
    user.setMail(mail);
    userDTO.setMail(mail);
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
    Mockito.when(userConverter.createDTO(user)).thenReturn(userDTO);
    Assertions.assertEquals(authenticationService.getUser(mail), userDTO);
  }

  @Test
  public void shouldNullWhenUserNotFoundTest() {
    String mail = "test@test.com";
    User user = new User();
    user.setMail(mail);
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.empty());
    Assertions.assertNull(authenticationService.getUser(mail));
  }

  @Test
  public void shouldTrueWhenRegisterUserTest() {
    String mail = "test@test.com";
    UserDTO userDTO = new UserDTO();
    userDTO.setMail(mail);
    Mockito.when(userRepository.save(any(User.class))).thenReturn(null);
    Assertions.assertTrue(authenticationService.register(userDTO));
  }

  @Test
  public void shouldFalseWhenRegisterUserTest() {
    String mail = "test@test.com";
    UserDTO userDTO = new UserDTO();
    userDTO.setMail(mail);
    Mockito.when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);
    Assertions.assertFalse(authenticationService.register(userDTO));
  }

  @Test
  public void shouldNotNullWhenUserLoginTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setMail("test@test.com");
    userDTO.setPassword("Testt!123");
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(userRepository.findByMail(userDTO.getMail())).thenReturn(Optional.of(new User()));
    Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    Assertions.assertNotNull(authenticationService.login(userDTO));
  }

  @Test
  public void shouldNullWhenUserLoginTest() {
    UserDTO userDTO = new UserDTO();
    userDTO.setMail("test@test.com");
    userDTO.setPassword("Testt!123");
    Mockito.when(userRepository.findByMail(userDTO.getMail())).thenReturn(Optional.of(new User()));
    Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(Mockito.mock(AuthenticationException.class));
    Assertions.assertThrows(
        AuthenticationException.class,
        () -> {
          authenticationService.login(userDTO);
        });
  }

  @Test
  public void shouldNotNullWhenRefreshTokenTest() {
    String mail = "test@test.com";
    User user = new User();
    user.setMail(mail);
    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
    refreshTokenDTO.setToken("token");
    Mockito.when(jwtService.extractUserName("token")).thenReturn("test@test.com");
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.of(user));
    Mockito.when(jwtService.isTokenValid("token", user)).thenReturn(true);
    Mockito.when(jwtService.generateToken(user)).thenReturn("newToken");
    Assertions.assertNotNull(authenticationService.refreshToken(refreshTokenDTO));
  }

  @Test
  public void shouldNullWhenTokenIsNotValidTest() {
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
