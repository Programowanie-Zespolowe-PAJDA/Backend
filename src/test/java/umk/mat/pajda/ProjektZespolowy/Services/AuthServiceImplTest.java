package umk.mat.pajda.ProjektZespolowy.Services;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthServiceImpl;

public class AuthServiceImplTest {
  @Mock private UserRepository userRepository;

  @InjectMocks private AuthServiceImpl authService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldSuccessTest() {
    String mail = "test@test.com";
    User testUser = new User();
    testUser.setMail(mail);
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.of(testUser));
    UserDetails userDetails = authService.userDetailsService().loadUserByUsername(mail);

    Assertions.assertEquals(testUser.getMail(), userDetails.getUsername());
  }

  @Test
  public void shouldThrowUsernameNotFoundExceptionWhenUserNotFoundTest() {
    String mail = "test@test.com";
    Mockito.when(userRepository.findByMail(mail)).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UsernameNotFoundException.class,
        () -> {
          authService.userDetailsService().loadUserByUsername(mail);
        });
  }
}
