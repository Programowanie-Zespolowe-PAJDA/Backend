package umk.mat.pajda.ProjektZespolowy.services;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.TipRepository;

public class TipServiceTest {

  @Mock private TipRepository tipRepository;
  @Mock private TipConverter tipConverter;

  @Mock private RestTemplate restTemplate;

  private TipService tipService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    tipService = new TipService(tipConverter, tipRepository);
    tipService.setRestTemplate(restTemplate);
  }

  @Test
  public void shouldSuccessWhenGetTokenTest() throws JsonProcessingException {
    String body = "{\"access_token\": \"token\"}";
    ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

    Mockito.when(
            restTemplate.exchange(
                eq("https://secure.snd.payu.com/pl/standard/user/oauth/authorize"),
                eq(HttpMethod.POST),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(response);

    Assertions.assertEquals("token", tipService.getToken());
  }

  @Test
  public void shouldSuccessWhenCreatePaymentTest() throws JsonProcessingException {
    String bodyToken = "{\"access_token\": \"token\"}";
    ResponseEntity<String> responseToken = new ResponseEntity<>(bodyToken, HttpStatus.OK);
    String bodyCreatePayment = "{\"redirect_url\": \"redirecturl\"}";
    ResponseEntity<String> responseCreatePayment =
        new ResponseEntity<>(bodyCreatePayment, HttpStatus.valueOf(302));
    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setHashRevID("127.0.0.1");
    opinionPostDTO.setComment("fajne");
    opinionPostDTO.setRating(5);
    opinionPostDTO.setUserID(1);
    opinionPostDTO.setClientName("Adrian");
    opinionPostDTO.setAmount(500);
    opinionPostDTO.setCurrency("PLN");

    Mockito.when(
            restTemplate.exchange(
                eq("https://secure.snd.payu.com/pl/standard/user/oauth/authorize"),
                eq(HttpMethod.POST),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(responseToken);
    Mockito.when(
            restTemplate.exchange(
                eq("https://secure.snd.payu.com/api/v2_1/orders"),
                eq(HttpMethod.POST),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(responseCreatePayment);

    Assertions.assertEquals(
        responseCreatePayment, tipService.createPayment(opinionPostDTO, "127.0.0.1"));
  }
}
