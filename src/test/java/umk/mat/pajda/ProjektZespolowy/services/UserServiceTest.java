package umk.mat.pajda.ProjektZespolowy.services;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import umk.mat.pajda.ProjektZespolowy.DTO.UserGetDTO;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.UserConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

public class UserServiceTest {

  private UserService userService;

  @Mock private UserConverter userConverter;

  @Mock private UserRepository userRepository;

  @Mock private ReviewRepository reviewRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userConverter, reviewRepository, userRepository);
  }

  @Test
  public void shouldSuccessWhenGetAllUsersTest() {
    UserGetDTO user1 = new UserGetDTO();
    UserGetDTO user2 = new UserGetDTO();
    user1.setName("Test1");
    user2.setName("Test2");
    List<UserGetDTO> expected = List.of(user1, user2);

    Mockito.when(userService.getAllUsers()).thenReturn(expected);

    List<UserGetDTO> list = userService.getAllUsers();
    Assertions.assertEquals(expected.get(0).getName(), list.get(0).getName());
    Assertions.assertEquals(expected.get(1).getName(), list.get(1).getName());
  }

  @Test
  public void shouldSuccessWhenGetUserByIdTest() {
    User user = new User();
    user.setId(1);
    UserGetDTO userGetDTO = new UserGetDTO();
    userGetDTO.setId(1);

    Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
    Mockito.when(userConverter.createDTO(user)).thenReturn(userGetDTO);

    Assertions.assertEquals(userGetDTO.getId(), userService.getUser(1).getId());
  }

  @Test
  public void shouldFailWhenGetUserByIdTest() {
    Mockito.when(userRepository.findById(1)).thenReturn(null);

    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          userService.getUser(1);
        });
  }

  @Test
  public void shouldSuccessWhenGetUserByEmailTest() {
    User user = new User();
    user.setMail("mail");

    Mockito.when(userRepository.findByMail("mail")).thenReturn(Optional.of(user));

    Assertions.assertEquals(user.getMail(), userService.getUser("mail").getMail());
  }

  @Test
  public void shouldFailWhenGetUserByEmailTest() {
    Mockito.when(userRepository.findByMail("mail")).thenReturn(null);

    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          userService.getUser("mail");
        });
  }

  @Test
  public void shouldSuccessWhenGetUserDTOByEmailTest() {
    User user = new User();
    user.setMail("mail");
    UserGetDTO userGetDTO = new UserGetDTO();
    userGetDTO.setMail("mail");

    Mockito.when(userRepository.findByMail("mail")).thenReturn(Optional.of(user));
    Mockito.when(userConverter.createDTO(user)).thenReturn(userGetDTO);

    Assertions.assertEquals(userGetDTO.getMail(), userService.getUserDTO("mail").getMail());
  }

  @Test
  public void shouldFailWhenGetUserDTOByEmailTest() {
    Mockito.when(userRepository.findByMail("mail")).thenReturn(null);

    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          userService.getUserDTO("mail");
        });
  }
}
