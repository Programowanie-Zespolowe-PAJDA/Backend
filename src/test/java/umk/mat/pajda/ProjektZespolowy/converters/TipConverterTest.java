package umk.mat.pajda.ProjektZespolowy.converters;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.TipGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.TipPatchPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@ActiveProfiles("tests")
public class TipConverterTest {

  private TipConverter tipConverter;

  @Mock private UserRepository userRepository;

  @BeforeEach
  public void setUp() throws Exception {
    openMocks(this);

    tipConverter = new TipConverter(userRepository);
  }

  @Test
  public void shouldCreateTipDTO() {
    LocalDateTime localDateTime = LocalDateTime.now();

    User user = new User();
    user.setSurname("Nowak");
    user.setId(321);
    user.setName("Tomek");
    user.setMail("wojtek@mail.com");

    Tip tip = new Tip();
    tip.setId(123);
    tip.setCurrency("PLN");
    tip.setAmount(13213.44F);
    tip.setCreatedAt(localDateTime);
    tip.setPaidWith("dasfas");
    tip.setUser(user);

    TipGetDTO tipDTO = tipConverter.createDTO(tip);

    TipGetDTO tipDTOShouldBe = new TipGetDTO();

    tipDTOShouldBe.setCurrency("PLN");
    tipDTOShouldBe.setId(123);
    tipDTOShouldBe.setUserId(321);
    tipDTOShouldBe.setAmount(13213.44F);
    tipDTOShouldBe.setPaidWith("dasfas");
    tipDTOShouldBe.setCreatedAt(localDateTime);

    Assertions.assertEquals(tipDTOShouldBe.toString(), tipDTO.toString());
  }

  @Test
  public void shouldCreateTip() {
    TipPatchPostDTO tipDTO = new TipPatchPostDTO();
    tipDTO.setCurrency("PLN");
    tipDTO.setAmount(13213.44F);
    tipDTO.setPaidWith("dasfas");
    tipDTO.setUserId(231);

    User user = new User();

    user.setSurname("Nowak");
    user.setName("Tomek");
    user.setMail("wojtek@mail.com");

    when(userRepository.findById(231)).thenReturn(Optional.of(user));

    Tip tip = tipConverter.createEntity(tipDTO);

    Tip tipShouldBe = new Tip();

    tipShouldBe.setCurrency("PLN");
    tipShouldBe.setAmount(13213.44F);
    tipShouldBe.setPaidWith("dasfas");
    tipShouldBe.setUser(user);

    Assertions.assertEquals(tip.toString2(), tipShouldBe.toString2());
  }

  @Test
  public void shouldCreateReviewArchiveDTOList() {
    LocalDateTime localDateTime1 = LocalDateTime.of(123, 3, 2, 3, 3);
    LocalDateTime localDateTime2 = LocalDateTime.of(1233, 1, 4, 2, 3);
    LocalDateTime localDateTime3 = LocalDateTime.of(1, 8, 2, 5, 3);

    User user1 = new User();
    User user2 = new User();
    User user3 = new User();

    user1.setSurname("Nowak");
    user1.setId(321);
    user1.setName("Tomek");
    user1.setMail("wojtek@mail.com");

    user2.setSurname("Lewandowski");
    user2.setId(2);
    user2.setName("Marek");
    user2.setMail("marek@mail.com");

    user3.setSurname("Nikolas");
    user3.setId(3);
    user3.setName("Tom");
    user3.setMail("tom@mail.com");

    Tip tip1 = new Tip();
    Tip tip2 = new Tip();
    Tip tip3 = new Tip();

    tip1.setId(123);
    tip1.setCurrency("PLN");
    tip1.setAmount(13213.44F);
    tip1.setCreatedAt(localDateTime1);
    tip1.setPaidWith("dasfas");
    tip1.setUser(user1);

    tip2.setId(534);
    tip2.setCurrency("EUR");
    tip2.setAmount(423.4F);
    tip2.setCreatedAt(localDateTime2);
    tip2.setPaidWith("5555234");
    tip2.setUser(user2);

    tip3.setId(45);
    tip3.setCurrency("WER");
    tip3.setAmount(443.4F);
    tip3.setCreatedAt(localDateTime3);
    tip3.setPaidWith("r4fs");
    tip3.setUser(user3);

    List<Tip> listTip = List.of(tip1, tip2, tip3);

    List<TipGetDTO> listTipDTO = tipConverter.createTipDTOList(listTip);

    TipGetDTO tipDTO1 = new TipGetDTO();
    TipGetDTO tipDTO2 = new TipGetDTO();
    TipGetDTO tipDTO3 = new TipGetDTO();

    tipDTO1.setCurrency("PLN");
    tipDTO1.setId(123);
    tipDTO1.setUserId(321);
    tipDTO1.setAmount(13213.44F);
    tipDTO1.setPaidWith("dasfas");
    tipDTO1.setCreatedAt(localDateTime1);

    tipDTO2.setCurrency("EUR");
    tipDTO2.setId(534);
    tipDTO2.setUserId(2);
    tipDTO2.setAmount(423.4F);
    tipDTO2.setPaidWith("5555234");
    tipDTO2.setCreatedAt(localDateTime2);

    tipDTO3.setCurrency("WER");
    tipDTO3.setId(45);
    tipDTO3.setUserId(3);
    tipDTO3.setAmount(443.4F);
    tipDTO3.setPaidWith("r4fs");
    tipDTO3.setCreatedAt(localDateTime3);

    List<TipGetDTO> listTipDTOShouldBe = List.of(tipDTO1, tipDTO2, tipDTO3);

    Assertions.assertEquals(listTipDTO.get(0).toString(), listTipDTOShouldBe.get(0).toString());
    Assertions.assertEquals(listTipDTO.get(1).toString(), listTipDTOShouldBe.get(1).toString());
    Assertions.assertEquals(listTipDTO.get(2).toString(), listTipDTOShouldBe.get(2).toString());
  }
}
