package umk.mat.pajda.ProjektZespolowy.converters;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.UserConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@ActiveProfiles("tests")
public class UserConverterTest {

  private UserConverter userConverter;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private UserRepository userRepository;

  @BeforeEach
  public void setUp() throws Exception {
    openMocks(this);

    userConverter = new UserConverter(passwordEncoder, userRepository);
  }

  @Test
  public void shouldCreateUserDTO() {
    User user = new User();
    user.setId(123);
    user.setName("Kuba");
    user.setSurname("Nowak");
    user.setPassword("Krowa");
    user.setRole("AAAA");
    user.setMail("krowa@email.com");
    user.setLocation("Poznan");

    UserGetDTO userDTO = userConverter.createDTO(user);

    UserGetDTO userDTOShouldBe = new UserGetDTO();
    userDTOShouldBe.setId(123);
    userDTOShouldBe.setName("Kuba");
    userDTOShouldBe.setSurname("Nowak");
    userDTOShouldBe.setMail("krowa@email.com");
    userDTOShouldBe.setLocation("Poznan");

    Assertions.assertEquals(userDTO.toString(), userDTOShouldBe.toString());
  }

  @Test
  public void shouldCreateUser() {
    RegisterDTO registerDTO = new RegisterDTO();
    registerDTO.setName("Kuba");
    registerDTO.setSurname("Nowak");
    registerDTO.setMail("krowa@email.com");
    registerDTO.setPassword("Pajacyk");
    registerDTO.setRetypedPassword("Pajacyk");
    registerDTO.setLocation("Poznan");

    when(passwordEncoder.encode("Pajacyk")).thenReturn("Bru bru bru");

    User user = userConverter.createEntity(registerDTO);

    User userShouldBe = new User();
    userShouldBe.setName("Kuba");
    userShouldBe.setSurname("Nowak");
    userShouldBe.setMail("krowa@email.com");
    userShouldBe.setPassword("Bru bru bru");
    userShouldBe.setLocation("Poznan");
    userShouldBe.setRole("ROLE_USER");

    Assertions.assertEquals(user.toString2(), userShouldBe.toString2());
  }

  @Test
  public void shouldCreateUserDTOList() {
    User user1 = new User();
    User user2 = new User();
    User user3 = new User();

    user1.setId(123);
    user1.setName("Kuba");
    user1.setSurname("Nowak");
    user1.setMail("krowa@email.com");
    user1.setPassword("Bru bru bru");
    user1.setLocation("Poznan");
    user1.setRole("AAAA");

    user2.setId(123);
    user2.setName("Morgana");
    user2.setSurname("Tomiwojarzer");
    user2.setMail("morgi@email.com");
    user2.setPassword("Nalesnik");
    user2.setLocation("Piła");
    user2.setRole("BBBB");

    user3.setId(123);
    user3.setName("Jakub");
    user3.setSurname("Wrona");
    user3.setMail("wronek@email.com");
    user3.setPassword("QWERTY123.");
    user3.setLocation("Kraków");
    user3.setRole("CCCC");

    List<User> listUser = List.of(user1, user2, user3);

    List<UserGetDTO> listUserDTO = userConverter.createUserDTOList(listUser);

    UserGetDTO userDTOShouldBe1 = new UserGetDTO();
    UserGetDTO userDTOShouldBe2 = new UserGetDTO();
    UserGetDTO userDTOShouldBe3 = new UserGetDTO();

    userDTOShouldBe1.setId(123);
    userDTOShouldBe1.setName("Kuba");
    userDTOShouldBe1.setSurname("Nowak");
    userDTOShouldBe1.setMail("krowa@email.com");
    userDTOShouldBe1.setLocation("Poznan");

    userDTOShouldBe2.setId(123);
    userDTOShouldBe2.setName("Morgana");
    userDTOShouldBe2.setSurname("Tomiwojarzer");
    userDTOShouldBe2.setMail("morgi@email.com");
    userDTOShouldBe2.setLocation("Piła");

    userDTOShouldBe3.setId(123);
    userDTOShouldBe3.setName("Jakub");
    userDTOShouldBe3.setSurname("Wrona");
    userDTOShouldBe3.setMail("wronek@email.com");
    userDTOShouldBe3.setLocation("Kraków");

    List<UserGetDTO> listUserDTOShouldBe =
        List.of(userDTOShouldBe1, userDTOShouldBe2, userDTOShouldBe3);

    Assertions.assertEquals(listUserDTO.get(0).toString(), listUserDTOShouldBe.get(0).toString());
    Assertions.assertEquals(listUserDTO.get(1).toString(), listUserDTOShouldBe.get(1).toString());
    Assertions.assertEquals(listUserDTO.get(2).toString(), listUserDTOShouldBe.get(2).toString());
  }

  @Test
  public void shouldUpdateInformationsOfEntity() {
    String mail = "Andzejek@email.com";
    UserPatchInformationsDTO userPatchInformationsDTO = new UserPatchInformationsDTO();
    userPatchInformationsDTO.setName("Matylda");
    userPatchInformationsDTO.setSurname("Nikkkkkk");
    userPatchInformationsDTO.setLocation("Poznan123");

    User user = new User();

    user.setId(123);
    user.setName("Kuba");
    user.setSurname("Nowak");
    user.setMail(mail);
    user.setPassword("Bru bru bru");
    user.setLocation("Poznan");
    user.setRole("AAAA");

    when(userRepository.findByMail("Andzejek@email.com")).thenReturn(Optional.of(user));

    User userShouldBe = new User();

    userShouldBe.setId(123);
    userShouldBe.setName("Matylda");
    userShouldBe.setSurname("Nikkkkkk");
    userShouldBe.setMail(mail);
    userShouldBe.setPassword("Bru bru bru");
    userShouldBe.setLocation("Poznan123");
    userShouldBe.setRole("AAAA");

    User userFromMethod = userConverter.updateInformationsOfEntity(userPatchInformationsDTO, mail);

    Assertions.assertEquals(userFromMethod.toString2(), userShouldBe.toString2());
  }

  @Test
  public void shouldUpdatePasswordOfEntity() {
    UserPatchPasswordDTO userPatchPasswordDTO = new UserPatchPasswordDTO();
    userPatchPasswordDTO.setPassword("Krowa123312%%%$#");
    userPatchPasswordDTO.setRetypedPassword("Krowa123312%%%$#");

    String email = "adam@gmail.com";

    User user = new User();
    user.setId(123);
    user.setName("Kuba");
    user.setSurname("Nowak");
    user.setMail(email);
    user.setPassword("Bru bru bru");
    user.setLocation("Poznan");
    user.setRole("AAAA");

    when(userRepository.findByMail(email)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("Krowa123312%%%$#")).thenReturn("qwert");

    User user2 = new User();
    user2.setId(123);
    user2.setName("Kuba");
    user2.setSurname("Nowak");
    user2.setMail(email);
    user2.setPassword("qwert");
    user2.setLocation("Poznan");
    user2.setRole("AAAA");

    User userFromMethod = userConverter.updatePasswordOfEntity(userPatchPasswordDTO, email);

    Assertions.assertEquals(userFromMethod.toString2(), user2.toString2());
  }

  @Test
  public void shouldUpdateEmailOfEntity() {
    UserPatchEmailDTO userPatchEmailDTO = new UserPatchEmailDTO();
    String email = "tejko@email.com";

    userPatchEmailDTO.setMail("tejko2@email.com");

    User user = new User();
    user.setId(123);
    user.setName("Kuba");
    user.setSurname("Nowak");
    user.setMail(email);
    user.setPassword("Bru bru bru");
    user.setLocation("Poznan");
    user.setRole("AAAA");

    when(userRepository.findByMail(email)).thenReturn(Optional.of(user));

    User userFromMethod = userConverter.updateEmailOfEntity(userPatchEmailDTO.getMail(), user);

    User userShouldBe = new User();
    userShouldBe.setId(123);
    userShouldBe.setName("Kuba");
    userShouldBe.setSurname("Nowak");
    userShouldBe.setMail("tejko2@email.com");
    userShouldBe.setPassword("Bru bru bru");
    userShouldBe.setLocation("Poznan");
    userShouldBe.setRole("AAAA");

    Assertions.assertEquals(userShouldBe.toString2(), userFromMethod.toString2());
  }
}
