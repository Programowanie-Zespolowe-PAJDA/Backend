package umk.mat.pajda.ProjektZespolowy.services;

import java.time.LocalDateTime;
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

@SpringBootTest
@ActiveProfiles("tests")
@TestPropertySource(
    properties = {
      "FIXEDSALT_IPHASH = $2a$10$9elrbM0La5ooQgMP7i9yjO",
      "SHOP_ID = shop_id",
      "CLIENT_SECRET = client_secret",
      "CLIENT_ID = client_id",
      "profile = tests"
    })
public class TipServiceStatTest {

  @Autowired private TipService tipService;

  @Autowired private TipConverter tipConverter;

  @Autowired private TipRepository tipRepository;

  @Autowired private UserRepository userRepository;

  @Test
  @Transactional
  @Rollback
  public void testCreationOfTipStatisticsGetDTOManual() {

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
    tip.setCurrency("PLN");
    tip.setUser(userRepository.findByMail("test@gmail.com").get());
    tip.setCreatedAt(LocalDateTime.now());
    tipRepository.save(tip);

    Tip tip2 = new Tip();
    tip2.setId("5");
    tip2.setAmount(4500);
    tip2.setCurrency("PLN");
    tip2.setUser(userRepository.findByMail("test@gmail.com").get());
    tip2.setCreatedAt(LocalDateTime.now().minusMonths(2));
    tipRepository.save(tip2);

    Tip tip3 = new Tip();
    tip3.setId("6");
    tip3.setAmount(20000);
    tip3.setCurrency("PLN");
    tip3.setUser(userRepository.findByMail("test@gmail.com").get());
    tip3.setCreatedAt(LocalDateTime.now().minusMonths(2));
    tipRepository.save(tip3);

    Tip tip4 = new Tip();
    tip4.setId("1");
    tip4.setAmount(15000);
    tip4.setCurrency("PLN");
    tip4.setUser(userRepository.findByMail("test@gmail.com").get());
    tip4.setCreatedAt(LocalDateTime.now().minusMonths(13));
    tipRepository.save(tip4);

    TipStatisticsGetDTO tipStatisticsGetDTO =
        tipService.getStatistics(userRepository.findByMail("test@gmail.com").get().getMail());
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
