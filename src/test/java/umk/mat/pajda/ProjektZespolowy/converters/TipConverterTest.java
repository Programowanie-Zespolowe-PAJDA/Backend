package umk.mat.pajda.ProjektZespolowy.converters;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import umk.mat.pajda.ProjektZespolowy.DTO.TipGetDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.Status;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

public class TipConverterTest {

  @Mock private ReviewRepository reviewRepository;
  @Mock private UserRepository userRepository;

  @InjectMocks private TipConverter tipConverter;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    tipConverter = new TipConverter(reviewRepository);
  }

  @Test
  public void shouldSuccessWhenCreateDTOTest() {
    LocalDateTime localDateTime = LocalDateTime.now();
    User user = new User();
    user.setId(2);
    Tip tip = new Tip();
    tip.setCreatedAt(localDateTime);
    tip.setUser(user);
    tip.setId("1");
    tip.setAmount(500);
    tip.setCurrency("PLN");
    tip.setPaidWith("BLIK");
    TipGetDTO expectedTipDTO = new TipGetDTO();
    expectedTipDTO.setCreatedAt(localDateTime);
    expectedTipDTO.setUserId(user.getId());
    expectedTipDTO.setId("1");
    expectedTipDTO.setAmount(500);
    expectedTipDTO.setCurrency("PLN");
    expectedTipDTO.setPaidWith("BLIK");

    TipGetDTO tipGetDTO = tipConverter.createDTO(tip);
    Assertions.assertEquals(tipGetDTO.getCreatedAt(), expectedTipDTO.getCreatedAt());
    Assertions.assertEquals(tipGetDTO.getId(), expectedTipDTO.getId());
    Assertions.assertEquals(tipGetDTO.getAmount(), expectedTipDTO.getAmount());
    Assertions.assertEquals(tipGetDTO.getCurrency(), expectedTipDTO.getCurrency());
    Assertions.assertEquals(tipGetDTO.getPaidWith(), expectedTipDTO.getPaidWith());
    Assertions.assertEquals(tipGetDTO.getUserId(), expectedTipDTO.getUserId());
  }

  @Test
  public void shouldSuccessWhenCreateEntityTest() {
    User user = new User();
    Review review = new Review();
    review.setUser(user);
    Tip expectedTip = new Tip();
    expectedTip.setId("payoutId");
    expectedTip.setUser(user);
    expectedTip.setCurrency("PLN");
    expectedTip.setPaidWith("BLIK");
    expectedTip.setAmount(500);

    Mockito.when(reviewRepository.findById("orderId")).thenReturn(Optional.of(review));
    Tip tip = tipConverter.createEntity("payoutId", "orderId", "500", "BLIK", "PLN", "1");

    Assertions.assertEquals(expectedTip.getId(), tip.getId());
    Assertions.assertEquals(expectedTip.getUser(), tip.getUser());
    Assertions.assertEquals(expectedTip.getCurrency(), tip.getCurrency());
    Assertions.assertEquals(expectedTip.getPaidWith(), tip.getPaidWith());
    Assertions.assertEquals(expectedTip.getAmount(), tip.getAmount());
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
    tip.setId("123");
    tip.setCurrency("PLN");
    tip.setAmount(13213);
    tip.setCreatedAt(localDateTime);
    tip.setPaidWith("dasfas");
    tip.setUser(user);

    TipGetDTO tipDTO = tipConverter.createDTO(tip);

    TipGetDTO tipDTOShouldBe = new TipGetDTO();

    tipDTOShouldBe.setCurrency("PLN");
    tipDTOShouldBe.setId("123");
    tipDTOShouldBe.setUserId(321);
    tipDTOShouldBe.setAmount(13213);
    tipDTOShouldBe.setPaidWith("dasfas");
    tipDTOShouldBe.setCreatedAt(localDateTime);

    Assertions.assertEquals(tipDTOShouldBe.toString(), tipDTO.toString());
  }

  @Test
  public void shouldCreateTip() {
    User user = new User();

    user.setSurname("Nowak");
    user.setName("Tomek");
    user.setMail("wojtek@mail.com");

    Review review = new Review();

    review.setId("1233456789");
    review.setRating(4);
    review.setComment("Witam");
    review.setCreatedAt(LocalDateTime.now());
    review.setClientName("Wiesiek");
    review.setHashRevID("21332fs");
    review.setStatus(Status.COMPLETED);
    review.setUser(user);

    // when(userRepository.findById(231)).thenReturn(Optional.of(user));
    when(reviewRepository.findById("2222")).thenReturn(Optional.of(review));

    Tip tip = tipConverter.createEntity("222", "2222", "13213", "dasfas", "PLN", "2.3");

    Tip tipShouldBe = new Tip();

    tipShouldBe.setId("222");
    tipShouldBe.setUser(review.getUser());

    tipShouldBe.setPaidWith("dasfas");
    tipShouldBe.setCurrency("PLN");
    tipShouldBe.setAmount(13213);
    tipShouldBe.setRealAmount(Math.round(13213 / (float) 2.3));
    tipShouldBe.setReview(review);

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

    tip1.setId("123");
    tip1.setCurrency("PLN");
    tip1.setAmount(13213);
    tip1.setCreatedAt(localDateTime1);
    tip1.setPaidWith("dasfas");
    tip1.setUser(user1);

    tip2.setId("534");
    tip2.setCurrency("EUR");
    tip2.setAmount(423);
    tip2.setCreatedAt(localDateTime2);
    tip2.setPaidWith("5555234");
    tip2.setUser(user2);

    tip3.setId("45");
    tip3.setCurrency("WER");
    tip3.setAmount(443);
    tip3.setCreatedAt(localDateTime3);
    tip3.setPaidWith("r4fs");
    tip3.setUser(user3);

    List<Tip> listTip = List.of(tip1, tip2, tip3);

    List<TipGetDTO> listTipDTO = tipConverter.createTipDTOList(listTip);

    TipGetDTO tipDTO1 = new TipGetDTO();
    TipGetDTO tipDTO2 = new TipGetDTO();
    TipGetDTO tipDTO3 = new TipGetDTO();

    tipDTO1.setCurrency("PLN");
    tipDTO1.setId("123");
    tipDTO1.setUserId(321);
    tipDTO1.setAmount(13213);
    tipDTO1.setPaidWith("dasfas");
    tipDTO1.setCreatedAt(localDateTime1);

    tipDTO2.setCurrency("EUR");
    tipDTO2.setId("534");
    tipDTO2.setUserId(2);
    tipDTO2.setAmount(423);
    tipDTO2.setPaidWith("5555234");
    tipDTO2.setCreatedAt(localDateTime2);

    tipDTO3.setCurrency("WER");
    tipDTO3.setId("45");
    tipDTO3.setUserId(3);
    tipDTO3.setAmount(443);
    tipDTO3.setPaidWith("r4fs");
    tipDTO3.setCreatedAt(localDateTime3);

    List<TipGetDTO> listTipDTOShouldBe = List.of(tipDTO1, tipDTO2, tipDTO3);

    Assertions.assertEquals(listTipDTO.get(0).toString(), listTipDTOShouldBe.get(0).toString());
    Assertions.assertEquals(listTipDTO.get(1).toString(), listTipDTOShouldBe.get(1).toString());
    Assertions.assertEquals(listTipDTO.get(2).toString(), listTipDTOShouldBe.get(2).toString());
  }
}
