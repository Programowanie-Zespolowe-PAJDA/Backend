package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;

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
    ResponseEntity response = new ResponseEntity<>("{\"orderId\": \"dsa4324\"}", HttpStatus.OK);

    Mockito.when(reviewService.addReview(opinionPostDTO, "dsa4324")).thenReturn(true);
    Mockito.when(tipService.createPayment(opinionPostDTO, "127.0.0.1")).thenReturn(response);

    Assertions.assertEquals(opinionService.addOpinion(opinionPostDTO, "127.0.0.1"), response);
  }
}
