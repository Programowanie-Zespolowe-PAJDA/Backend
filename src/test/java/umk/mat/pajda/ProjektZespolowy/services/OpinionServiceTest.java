package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;

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
    ResponseEntity<String> response =
        new ResponseEntity<>("{\"orderId\": \"dsa4324\"}", HttpStatus.OK);

    Mockito.when(reviewService.addReview(opinionPostDTO, "dsa4324")).thenReturn(true);
    Mockito.when(tipService.createPayment(opinionPostDTO, "127.0.0.1", 500, "1"))
        .thenReturn(response);

    Assertions.assertEquals(
        opinionService.addOpinion(opinionPostDTO, "127.0.0.1", 500, "1"), response);
  }

  @Test
  public void shouldSuccessWhenGetOpinionsTest() {
    Tip tip1 = new Tip();
    tip1.setRealAmount(500);
    tip1.setCurrency("PLN");
    Review review1 = new Review();
    review1.setComment("komentarz1");
    review1.setClientName("klient1");
    review1.setTip(tip1);
    Tip tip2 = new Tip();
    tip2.setRealAmount(300);
    tip2.setCurrency("USD");
    Review review2 = new Review();
    review2.setComment("komentarz2");
    review2.setClientName("klient2");
    review2.setTip(tip2);
    List<Review> reviews = List.of(review1, review2);

    Mockito.when(reviewService.getAllReviewsByEmail("email")).thenReturn(reviews);
    List<OpinionGetDTO> list = opinionService.getOpinions("email");

    Assertions.assertEquals("komentarz1", list.get(0).getComment());
    Assertions.assertEquals("klient1", list.get(0).getClientName());
    Assertions.assertEquals(500, list.get(0).getAmount());
    Assertions.assertEquals("PLN", list.get(0).getCurrency());
    Assertions.assertEquals("komentarz2", list.get(1).getComment());
    Assertions.assertEquals("klient2", list.get(1).getClientName());
    Assertions.assertEquals(300, list.get(1).getAmount());
    Assertions.assertEquals("USD", list.get(1).getCurrency());
  }
}
