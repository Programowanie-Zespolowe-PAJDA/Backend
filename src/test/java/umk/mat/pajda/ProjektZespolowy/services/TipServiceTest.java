package umk.mat.pajda.ProjektZespolowy.services;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.TipRepository;

public class TipServiceTest {

  @Mock private TipRepository tipRepository;
  @Mock private TipConverter tipConverter;

  @Mock private RestTemplate restTemplate;

  private TipService tipService;

  private MockRestServiceServer server;

  private String token;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    tipService = new TipService(tipConverter, tipRepository);
    tipService.setRestTemplate(restTemplate);
    server = MockRestServiceServer.bindTo(restTemplate).build();
    String bodyToken = "{\"access_token\": \"token\"}";
    String bodyCreatePayment = "{\"redirect_url\": \"redirecturl\"}";
    server
        .expect(
            ExpectedCount.manyTimes(),
            requestTo("https://secure.snd.payu.com/pl/standard/user/oauth/authorize"))
        .andExpect(method(HttpMethod.POST))
        .andRespond(
            withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(bodyToken));
    server
        .expect(ExpectedCount.once(), requestTo("https://secure.snd.payu.com/api/v2_1/orders"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(header(HttpHeaders.AUTHORIZATION, "token"))
        .andRespond(
            withStatus(HttpStatus.valueOf(302))
                .contentType(MediaType.APPLICATION_JSON)
                .body(bodyCreatePayment));
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
