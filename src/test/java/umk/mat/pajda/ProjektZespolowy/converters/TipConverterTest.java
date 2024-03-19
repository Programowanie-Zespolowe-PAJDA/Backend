package umk.mat.pajda.ProjektZespolowy.converters;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umk.mat.pajda.ProjektZespolowy.DTO.TipGetDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

public class TipConverterTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private TipConverter tipConverter;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
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
}
