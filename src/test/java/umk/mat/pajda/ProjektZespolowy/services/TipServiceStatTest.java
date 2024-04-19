package umk.mat.pajda.ProjektZespolowy.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import umk.mat.pajda.ProjektZespolowy.DTO.TipStatisticsGetDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.TipRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Transactional
@Rollback
@SpringBootTest
@ActiveProfiles("tests")
@TestPropertySource(
    properties = {
      "FIXEDSALT_IPHASH = $2a$10$9elrbM0La5ooQgMP7i9yjO",
      "SHOP_ID = shop_id",
      "CLIENT_SECRET = client_secret",
      "CLIENT_ID = client_id",
      "profile = tests",
      "KEY_MD5 = key_md5",
      "ngrok.link = link",
            "GMAIL_APP_PASSWORD = gmail_app_password"

    })
public class TipServiceStatTest {

  @Autowired private TipService tipService;

  @Autowired private TipConverter tipConverter;

  @Autowired private TipRepository tipRepository;

  @Autowired private UserRepository userRepository;

  private static String currency = "PLN";

  @BeforeEach
  public void init() {
    User user = new User();
    user.setId(1000);
    user.setMail("test@gmail.com");
    user.setName("Adam");
    user.setSurname("Kowalski");
    user.setPassword("testpass123456789");
    user.setRole("ROLE_USER");
    userRepository.save(user);

    Tip tip = new Tip();
    tip.setId("4");
    tip.setAmount(2000);
    tip.setRealAmount(2000);
    tip.setCurrency("PLN");
    tip.setUser(userRepository.findByMail("test@gmail.com").get());
    tip.setCreatedAt(LocalDateTime.now());
    tipRepository.save(tip);

    Tip tip2 = new Tip();
    tip2.setId("5");
    tip2.setAmount(4500);
    tip2.setRealAmount(4500);
    tip2.setCurrency("PLN");
    tip2.setUser(userRepository.findByMail("test@gmail.com").get());
    tip2.setCreatedAt(LocalDateTime.now().minusMonths(2));
    tipRepository.save(tip2);

    Tip tip3 = new Tip();
    tip3.setId("6");
    tip3.setAmount(20000);
    tip3.setRealAmount(20000);
    tip3.setCurrency("PLN");
    tip3.setUser(userRepository.findByMail("test@gmail.com").get());
    tip3.setCreatedAt(LocalDateTime.now().minusMonths(2));
    tipRepository.save(tip3);

    Tip tip4 = new Tip();
    tip4.setId("1");
    tip4.setAmount(15000);
    tip4.setRealAmount(15000);
    tip4.setCurrency("PLN");
    tip4.setUser(userRepository.findByMail("test@gmail.com").get());
    tip4.setCreatedAt(LocalDateTime.now().minusMonths(13));
    tipRepository.save(tip4);
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOReturnValueOfNumberOfTipsValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(tipStatisticsGetDTO.getNumberOfTips() == 4);
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOValidReturnValueOfMinTipAmountValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(tipStatisticsGetDTO.getMinTipAmount() == 2000);
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOValidReturnValueOfMaxTipAmountValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(tipStatisticsGetDTO.getMaxTipAmount() == 20000);
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOValidReturnValueOfAvgTipAmountValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(tipStatisticsGetDTO.getAvgTipAmount() == 10375.0);
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOValidReturnValueOfNumberOfTipsValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(tipStatisticsGetDTO.getNumberOfTips() == 4);
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOValidReturnSumTipValueForEveryMonthGet0MonthValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(
        Month.of(LocalDateTime.now().getMonthValue())
            .toString()
            .equals(tipStatisticsGetDTO.getSumTipValueForEveryMonth().get(0).getMonth()));
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOValidReturnSumTipValueForEveryMonthGet0YearValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(
        String.valueOf(LocalDateTime.now().getYear())
            .equals(tipStatisticsGetDTO.getSumTipValueForEveryMonth().get(0).getYear()));
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOValidReturnSumTipValueForEveryMonthGet2MonthValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(
        Month.of(LocalDateTime.now().minusMonths(13).getMonthValue())
            .toString()
            .equals(tipStatisticsGetDTO.getSumTipValueForEveryMonth().get(2).getMonth()));
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOValidReturnSumTipValueForEveryMonthGet2YearValid() {
    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    assertTrue(
        String.valueOf(LocalDateTime.now().minusMonths(13).getYear())
            .equals(tipStatisticsGetDTO.getSumTipValueForEveryMonth().get(2).getYear()));
  }

  @Test
  public void testCreationOfTipStatisticsGetDTOManual() {

    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(
            userRepository.findByMail("test@gmail.com").get().getMail(), currency);
    System.out.println(tipStatisticsGetDTO);
    System.out.println(tipStatisticsGetDTO.getNumberOfTips());
    System.out.println(tipStatisticsGetDTO.getMinTipAmount());
    System.out.println(tipStatisticsGetDTO.getMaxTipAmount());
    System.out.println(tipStatisticsGetDTO.getAvgTipAmount());
    System.out.println(tipStatisticsGetDTO.getSumTipValueForEveryMonth());
    System.out.println(tipStatisticsGetDTO.getSumTipValueForEveryMonth().get(0).toString());
    System.out.println(tipStatisticsGetDTO.getSumTipValueForEveryMonth().get(1).toString());
  }
}
