package umk.mat.pajda.ProjektZespolowy.services;

import static org.mockito.ArgumentMatchers.eq;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.TipRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

public class TipServiceTest {

  @Mock private TipRepository tipRepository;
  @Mock private TipConverter tipConverter;

  @Mock private UserService userService;

  @Mock private UserRepository userRepository;

  @Mock private ReviewService reviewService;

  @Mock private RestTemplate restTemplate;

  @InjectMocks private TipService tipService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
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
    tipService.setToken();

    Assertions.assertEquals(tipService.getToken(), "token");
  }

  @Test
  public void shouldSuccessWhenMakePayoutTest() throws JsonProcessingException {
    String body =
        "{ \"status\": { \"statusCode\": \"SUCCESS\"}, \"payout\": { \"payoutId\": \"payoutId\"}}";
    ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.CREATED);
    Review review = new Review();
    review.setId("reviewId");
    User user = new User();
    user.setReviewList(List.of(review));
    user.setBankAccountNumber("31241234235123413241234");
    user.setName("Andrzej");
    user.setSurname("Kowalski");

    Mockito.when(userService.getUserByReviewId("orderId")).thenReturn(user);
    Mockito.when(
            restTemplate.exchange(
                eq("https://secure.snd.payu.com/api/v2_1/payouts"),
                eq(HttpMethod.POST),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(response);

    Assertions.assertEquals("payoutId", tipService.makePayout("orderId", "500"));
  }

  @Test
  public void shouldSuccessWhenGetStatusTest() throws JsonProcessingException {
    String body = "{\"order\": { \"status\": \"COMPLETED\"}}";

    Assertions.assertEquals("COMPLETED", tipService.getStatus(body));
  }

  @Test
  public void shouldSuccessWhenGetOrderIdTest() throws JsonProcessingException {
    String body = "{\"order\": { \"orderId\": \"orderId\"}}";

    Assertions.assertEquals("orderId", tipService.getOrderId(body));
  }

  @Test
  public void shouldSuccessWhenVerifyNotificationTest() throws NoSuchAlgorithmException {
    String header =
        "sender=checkout;signature=d97a213ca2c077a3c4936889efc9f2e9;algorithm=MD5;content=DOCUMENT";
    String body = "{\"order\": { \"orderId\": \"orderId\"}}";

    Assertions.assertTrue(tipService.verifyNotification(body, header));
  }

  @Test
  public void shouldFailWhenBadSignatureVerifyNotificationTest() throws NoSuchAlgorithmException {
    String header =
        "sender=checkout;signature=d47d8a771d558c29285887febddd9327;algorithm=MD5;content=DOCUMENT";
    String body = "{\"order\": { \"orderId\": \"orderId\"}}";

    Assertions.assertFalse(tipService.verifyNotification(body, header));
  }

  @Test
  public void shouldFailWhenBadHeaderVerifyNotificationTest() throws NoSuchAlgorithmException {
    String header = "sender=checkout;algorithm=MD5;content=DOCUMENT";
    String body = "{\"order\": { \"orderId\": \"orderId\"}}";

    Assertions.assertFalse(tipService.verifyNotification(body, header));
  }

  @Test
  public void shouldSuccessWhenCreatePaymentTest() throws JsonProcessingException {
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
                eq("https://secure.snd.payu.com/api/v2_1/orders"),
                eq(HttpMethod.POST),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(responseCreatePayment);

    Assertions.assertEquals(
        responseCreatePayment, tipService.createPayment(opinionPostDTO, "127.0.0.1", 500));
  }

  @Test
  public void shouldSuccessWhenGetExchangeRateTest() throws JsonProcessingException {
    String body =
        "{ \"currencyPairs\": [{\"baseCurrency\": \"USD\", \"termCurrency\": \"PLN\", \"exchangeRate\": \"5.32\"}, {\"baseCurrency\": \"SKK\", \"termCurrency\": \"PLN\", \"exchangeRate\": \"1.32\"}]}";
    ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

    Mockito.when(
            restTemplate.exchange(
                eq(
                    "https://secure.snd.payu.com/api/v2_1/mcp-partners/6283a549-8b1a-430d-8a62-eea64327440e/fx-table"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(response);

    Assertions.assertEquals("5.32", tipService.getExchangeRate("USD"));
  }

  @Test
  public void shouldSuccessWhenAddTipTest() {
    User user = new User();
    Tip tip = new Tip();
    tip.setId("payoutId");
    tip.setUser(user);
    tip.setCurrency("PLN");
    tip.setCreatedAt(LocalDateTime.of(2012, Month.SEPTEMBER, 4, 15, 30));
    tip.setPaidWith("BLIK");
    tip.setAmount(500);

    Mockito.when(tipConverter.createEntity("payoutId", "orderId", "500", "BLIK", "PLN"))
        .thenReturn(tip);

    Assertions.assertTrue(tipService.addTip("payoutId", "orderId", "500", "blik", "PLN"));
  }

  @Test
  public void shouldSuccessWhenGetPaidWithTest() throws JsonProcessingException {
    String orderId = "orderId";
    String body = "{ \"transactions\": [{ \"payMethod\": { \"value\": \"blik\"}}]}";
    ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

    Mockito.when(
            restTemplate.exchange(
                eq("https://secure.snd.payu.com/api/v2_1/orders/" + orderId + "/transactions"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(response);

    Assertions.assertEquals("blik", tipService.getPaidWith(orderId));
  }

  @Test
  public void shouldSuccessWhenMakeRefundTest() throws JsonProcessingException {
    String orderId = "orderId";
    String body = "{ \"status\": { \"statusCode\": \"SUCCESS\"}}";
    ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

    Mockito.when(
            restTemplate.exchange(
                eq("https://secure.snd.payu.com/api/v2_1/orders/" + orderId + "/refunds"),
                eq(HttpMethod.POST),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(response);

    Assertions.assertTrue(tipService.makeRefund(orderId));
  }

  @Test
  public void shouldSuccessWhenGetCurrencyTest() throws JsonProcessingException {
    String body = "{\"order\": { \"description\": \"PLN\"}}";

    Assertions.assertEquals("PLN", tipService.getCurrency(body));
  }

  @Test
  public void shouldSuccessWhenGetAmountTest() throws JsonProcessingException {
    String body = "{\"order\": {\"totalAmount\": \"500\"}}";

    Assertions.assertEquals("500", tipService.getAmount(body));
  }

  @Test
  public void shouldSuccessWhenGetRealAmountTest1() throws JsonProcessingException {

    Assertions.assertEquals("453", tipService.getRealAmount("501", "dpkl"));
  }

  @Test
  public void shouldSuccessWhenGetRealAmountTest2() throws JsonProcessingException {

    Assertions.assertEquals("452", tipService.getRealAmount("500", "dpkl"));
  }

  @Test
  public void shouldSuccessWhenGetRealAmountTest3() throws JsonProcessingException {

    Assertions.assertEquals("452", tipService.getRealAmount("499", "dpkl"));
  }

  @Test
  public void shouldSuccessWhenChangeBearerAuthTest() throws JsonProcessingException {
    ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth("token");
    String body = "{\"access_token\": \"token\"}";
    ResponseEntity<String> responseToken = new ResponseEntity<>(body, HttpStatus.OK);

    Mockito.when(
            restTemplate.exchange(
                eq("https://secure.snd.payu.com/pl/standard/user/oauth/authorize"),
                eq(HttpMethod.POST),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(responseToken);
    Mockito.when(
            restTemplate.exchange(
                eq("link"),
                eq(HttpMethod.POST),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(response);

    Assertions.assertEquals(
        response, tipService.changeBearerAuth(headers, null, "link", HttpMethod.POST));
  }

  @Test
  public void shouldSuccessWhenCheckMinOfCurrencyTest1() throws JsonProcessingException {
    String body =
        "{ \"currencyPairs\": [{\"baseCurrency\": \"USD\", \"termCurrency\": \"PLN\", \"exchangeRate\": \"5.32\"}, {\"baseCurrency\": \"SKK\", \"termCurrency\": \"PLN\", \"exchangeRate\": \"1.32\"}]}";
    ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

    Mockito.when(
            restTemplate.exchange(
                eq(
                    "https://secure.snd.payu.com/api/v2_1/mcp-partners/6283a549-8b1a-430d-8a62-eea64327440e/fx-table"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(response);

    Assertions.assertTrue(tipService.checkMinOfCurrency("USD", 40));
  }

  @Test
  public void shouldSuccessWhenCheckMinOfCurrencyTest2() throws JsonProcessingException {
    String body =
        "{ \"currencyPairs\": [{\"baseCurrency\": \"USD\", \"termCurrency\": \"PLN\", \"exchangeRate\": \"1.32\"}, {\"baseCurrency\": \"SKK\", \"termCurrency\": \"PLN\", \"exchangeRate\": \"1.32\"}]}";
    ResponseEntity<String> response = new ResponseEntity<>(body, HttpStatus.OK);

    Mockito.when(
            restTemplate.exchange(
                eq(
                    "https://secure.snd.payu.com/api/v2_1/mcp-partners/6283a549-8b1a-430d-8a62-eea64327440e/fx-table"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()))
        .thenReturn(response);

    Assertions.assertFalse(tipService.checkMinOfCurrency("USD", 40));
  }

  @Test
  public void shouldSuccessWhenCheckMinOfCurrencyTest3() throws JsonProcessingException {
    Assertions.assertTrue(tipService.checkMinOfCurrency("PLN", 80));
  }

  @Test
  public void shouldSuccessWhenCheckMinOfCurrencyTest4() throws JsonProcessingException {
    Assertions.assertFalse(tipService.checkMinOfCurrency("PLN", 79));
  }
}
