package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;

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
public class OpinionServiceTest {

  @Mock private ReviewService reviewService;

  @Mock private TipService tipService;

  @InjectMocks private OpinionService opinionService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldSuccessWhenAddOpinionTest() throws JsonProcessingException {
    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setHashRevID("127.0.0.1");
    opinionPostDTO.setComment("fajne");
    opinionPostDTO.setRating(5);
    opinionPostDTO.setUserID(1);
    opinionPostDTO.setClientName("Adrian");
    opinionPostDTO.setAmount(500);
    opinionPostDTO.setCurrency("PLN");
    ResponseEntity response = new ResponseEntity<>(HttpStatus.OK);

    Mockito.when(reviewService.addReview(opinionPostDTO)).thenReturn(true);
    Mockito.when(tipService.createPayment(opinionPostDTO, "127.0.0.1")).thenReturn(response);

    Assertions.assertEquals(opinionService.addOpinion(opinionPostDTO, "127.0.0.1"), response);
  }
}
