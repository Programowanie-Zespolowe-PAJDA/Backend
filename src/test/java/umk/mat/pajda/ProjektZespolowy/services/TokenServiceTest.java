package umk.mat.pajda.ProjektZespolowy.services;

import java.util.Optional;
import org.junit.jupiter.api.*;
import org.mockito.*;
import umk.mat.pajda.ProjektZespolowy.entity.Token;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.repository.TokenRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

public class TokenServiceTest {

  @Mock private TokenRepository tokenRepository;
  @Mock private UserRepository userRepository;
  @InjectMocks private TokenService tokenService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldSuccessWhenCreateToken() {
    User user = new User();
    Token expectedToken = new Token();
    expectedToken.setUser(user);

    Token token = tokenService.createToken(user);
    Assertions.assertNotNull(token);
    Assertions.assertNotNull(token.getToken());
    Assertions.assertEquals(token.getUser(), expectedToken.getUser());
  }

  @Test
  public void shouldSuccessWhenGetTokenTest() {
    Token token = new Token();

    Mockito.when(tokenRepository.findByToken("test")).thenReturn(Optional.of(token));

    Assertions.assertEquals(token, tokenService.getToken("test"));
  }

  @Test
  public void shouldTokenNotFoundWhenGetTokenTest() {
    Mockito.when(tokenRepository.findByToken("test")).thenReturn(Optional.empty());

    Assertions.assertNull(tokenService.getToken("test"));
  }

  @Test
  public void shouldSuccesWhenConfirmTest() {
    Token token = new Token();
    User user = new User();
    user.setEnabled(false);

    Mockito.when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

    tokenService.confirm(token);
    Assertions.assertTrue(user.isEnabled());
  }

  @Test
  public void shouldTokenNotFoundWhenConfirmTest() {
    Token token = new Token();

    Mockito.when(userRepository.findByToken(token)).thenReturn(Optional.empty());

    Assertions.assertFalse(tokenService.confirm(token));
  }

  @Test
  public void shouldSuccessWhenUpdateToken(){
    Token token = new Token();
    User user = new User();

    Mockito.when(tokenRepository.findByUser(user)).thenReturn(Optional.of(token));

    Assertions.assertEquals("email", tokenService.updateToken("email", user).getNewEmail());

  }
}
