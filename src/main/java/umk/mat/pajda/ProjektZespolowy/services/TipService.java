package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.TipStatisticsGetDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.TipRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Service
public class TipService {
  private final Logger logger = LoggerFactory.getLogger(TipService.class);
  private final TipConverter tipConverter;
  private final TipRepository tipRepository;
  private final UserRepository userRepository;

  private final ReviewService reviewService;

  private final ReviewRepository reviewRepository;

  @Autowired(required = false)
  private EmailService emailService;

  private final UserService userService;

  private RestTemplate restTemplate;

  @Value("${SHOP_ID}")
  private String shopId;

  @Value("${CLIENT_ID}")
  private String clientId;

  @Value("${CLIENT_SECRET}")
  private String clientSecret;

  @Value("${KEY_MD5}")
  private String keyMd5;

  @Value("${profile}")
  private String profile;

  private String token = null;

  @Value("${ngrok.link}")
  private String link;

  private final Semaphore semaphore = new Semaphore(1, true);

  public TipService(
      TipConverter tipConverter,
      TipRepository tipRepository,
      UserRepository userRepository,
      ReviewService reviewService,
      ReviewRepository reviewRepository,
      UserService userService) {
    this.tipConverter = tipConverter;
    this.tipRepository = tipRepository;
    this.reviewRepository = reviewRepository;
    this.reviewService = reviewService;
    this.userService = userService;
    this.userRepository = userRepository;
    this.restTemplate = new RestTemplate();
  }

  public String makePayout(String orderId, String realAmount) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Map<String, Object> body = new HashMap<>();
    body.put("shopId", shopId);
    Map<String, Object> payout = new HashMap<>();
    payout.put("amount", realAmount);
    payout.put("description", "Napiwek");
    Map<String, Object> account = new HashMap<>();
    User user = userService.getUserByReviewId(orderId);
    if (user == null) {
      return null;
    }
    account.put("accountNumber", user.getBankAccountNumber());
    Map<String, Object> customerAddress = new HashMap<>();
    customerAddress.put("name", user.getName() + " " + user.getSurname());
    body.put("payout", payout);
    body.put("account", account);
    body.put("customerAddress", customerAddress);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    ResponseEntity<String> response;
    try {
      response =
          restTemplate.exchange(
              "https://secure.snd.payu.com/api/v2_1/payouts",
              HttpMethod.POST,
              request,
              String.class);
    } catch (HttpClientErrorException.Unauthorized e) {
      response =
          changeBearerAuth(
              headers, body, "https://secure.snd.payu.com/api/v2_1/payouts", HttpMethod.POST);
    }
    if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
      return null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(response.getBody());
    if (jsonNode.get("status").get("statusCode").asText().equals("SUCCESS")) {
      semaphore.release();
      return jsonNode.get("payout").get("payoutId").asText();
    } else {
      return null;
    }
  }

  public String getStatus(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(requestBody);
    return jsonNode.get("order").get("status").asText();
  }

  public String getOrderId(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(requestBody);
    return jsonNode.get("order").get("orderId").asText();
  }

  public boolean verifyNotification(String requestBody, String header)
      throws NoSuchAlgorithmException {
    if (requestBody == null || header == null) {
      return false;
    }
    String[] valuesOfHeader = header.split(";");
    Map<String, String> map = new HashMap<>();
    for (String value : valuesOfHeader) {
      String[] keyValue = value.split("=");
      if (keyValue.length == 2) {
        map.put(keyValue[0], keyValue[1]);
      } else {
        return false;
      }
    }
    if (!map.containsKey("signature")) {
      return false;
    }
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] messageDigest = md.digest((requestBody + keyMd5).getBytes());
    BigInteger bigInteger = new BigInteger(1, messageDigest);
    String expectedSignature = bigInteger.toString(16);
    while (expectedSignature.length() < 32) {
      expectedSignature = "0" + expectedSignature;
    }
    return expectedSignature.equals(map.get("signature"));
  }

  public String getToken() {
    return token;
  }

  public void setToken() throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.set("grant_type", "client_credentials");
    body.set("client_id", clientId);
    body.set("client_secret", clientSecret);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
    ResponseEntity<String> tokenResponse =
        restTemplate.exchange(
            "https://secure.snd.payu.com/pl/standard/user/oauth/authorize",
            HttpMethod.POST,
            request,
            String.class);
    if (!tokenResponse.getStatusCode().equals(HttpStatus.OK)) {
      token = null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(tokenResponse.getBody());
    token = jsonNode.get("access_token").asText();
  }

  public ResponseEntity<String> createPayment(
      OpinionPostDTO opinionPostDTO, String ip, int lastAmount, String exchangeRate)
      throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Map<String, Object> products = new HashMap<>();
    products.put("name", "Napiwek");
    products.put("quantity", "1");
    Map<String, Object> body = new HashMap<>();
    body.put("customerIp", ip);
    body.put("merchantPosId", clientId);
    body.put("currencyCode", "PLN");
    if ("prod".equals(profile)) {
      body.put("notifyUrl", "https://enapiwek-api.onrender.com/tip");
      body.put("continueUrl", "https://enapiwek.onrender.com/thankyou");
    } else {
      body.put("continueUrl", "http://localhost:5173/thankyou");
      body.put("notifyUrl", link + "tip");
    }
    body.put("additionalDescription", exchangeRate);
    body.put("totalAmount", String.valueOf(lastAmount));
    products.put("unitPrice", String.valueOf(lastAmount));
    body.put("description", opinionPostDTO.getCurrency());
    body.put("products", new Object[] {products});
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    ResponseEntity<String> response;
    try {
      response =
          restTemplate.exchange(
              "https://secure.snd.payu.com/api/v2_1/orders",
              HttpMethod.POST,
              request,
              String.class);
    } catch (HttpClientErrorException.Unauthorized e) {
      response =
          changeBearerAuth(
              headers, body, "https://secure.snd.payu.com/api/v2_1/orders", HttpMethod.POST);
    }
    if (!response.getStatusCode().equals(HttpStatus.FOUND)) {
      return null;
    }
    return response;
  }

  public String getExchangeRate(String currency) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<Object> request = new HttpEntity<>(headers);
    ResponseEntity<String> response;
    try {
      response =
          restTemplate.exchange(
              "https://secure.snd.payu.com/api/v2_1/mcp-partners/6283a549-8b1a-430d-8a62-eea64327440e/fx-table",
              HttpMethod.GET,
              request,
              String.class);

    } catch (HttpClientErrorException.Unauthorized e) {
      response =
          changeBearerAuth(
              headers,
              null,
              "https://secure.snd.payu.com/api/v2_1/mcp-partners/6283a549-8b1a-430d-8a62-eea64327440e/fx-table",
              HttpMethod.GET);
    }

    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      return null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(response.getBody());
    for (JsonNode node : jsonNode.get("currencyPairs")) {
      if (node.get("baseCurrency").asText().equals(currency)
          && node.get("termCurrency").asText().equals("PLN")) {
        return node.get("exchangeRate").asText();
      }
    }
    return null;
  }

  public boolean addTip(
      String payoutId,
      String orderId,
      String realAmount,
      String paidWith,
      String currency,
      String exchangeRate) {
    try {
      tipRepository.save(
          tipConverter.createEntity(
              payoutId, orderId, realAmount, paidWith, currency, exchangeRate));
    } catch (Exception e) {
      logger.error("addTip", e);
      return false;
    }
    return true;
  }

  public String getPaidWith(String orderId) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    HttpEntity<Object> request = new HttpEntity<>(headers);
    ResponseEntity<String> response;
    try {
      response =
          restTemplate.exchange(
              "https://secure.snd.payu.com/api/v2_1/orders/" + orderId + "/transactions",
              HttpMethod.GET,
              request,
              String.class);
    } catch (HttpClientErrorException.Unauthorized e) {
      response =
          changeBearerAuth(
              headers,
              null,
              "https://secure.snd.payu.com/api/v2_1/orders/" + orderId + "/transactions",
              HttpMethod.GET);
    }
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      return null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode node = objectMapper.readTree(response.getBody());
    for (JsonNode jsonNode : node.get("transactions")) {
      try {
        response =
            restTemplate.exchange(
                "https://secure.snd.payu.com/api/v2_1/paymethods",
                HttpMethod.GET,
                request,
                String.class);
      } catch (HttpClientErrorException.Unauthorized e) {
        response =
            changeBearerAuth(
                headers, null, "https://secure.snd.payu.com/api/v2_1/paymethods", HttpMethod.GET);
      }
      if (!response.getStatusCode().equals(HttpStatus.OK)) {
        return null;
      }
      for (JsonNode jsonNode2 : objectMapper.readTree(response.getBody()).get("payByLinks")) {
        if (jsonNode2.get("value").asText().equals(jsonNode.get("payMethod").get("value").asText())
            && jsonNode2.get("status").asText().equals("ENABLED")) {
          return jsonNode2.get("name").asText();
        }
      }
    }
    return null;
  }

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public TipStatisticsGetDTO getStatistics(String userName) {
    try {
      User user = userRepository.findByMail(userName).get();
      TipStatisticsGetDTO tipStatisticsGetDTO = new TipStatisticsGetDTO();
      Tip tipMin = tipRepository.findFirstByUserOrderByAmountDesc(user);
      tipStatisticsGetDTO.setMaxTipAmount(tipMin.getAmount());
      tipStatisticsGetDTO.setCurrency(tipMin.getCurrency());

      tipStatisticsGetDTO.setMinTipAmount(
          tipRepository.findFirstByUserOrderByAmountAsc(user).getAmount());
      tipStatisticsGetDTO.setAvgTipAmount(tipRepository.getAvgAmountForAllTips(user.getId()));
      tipStatisticsGetDTO.setSumTipValueForEveryMonth(
          tipRepository.getSumAmountForEachMonth(user.getId()));
      tipStatisticsGetDTO.setNumberOfTips(tipRepository.getNumberOfTips(user.getId()));

      return tipStatisticsGetDTO;
    } catch (Exception e) {
      logger.error("getStatistics", e);
      return null;
    }
  }

  public String getCurrency(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readTree(requestBody).get("order").get("description").asText();
  }

  public String getRealAmount() throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Object> request = new HttpEntity<>(headers);
    ResponseEntity<String> response;
    try {
      response =
          restTemplate.exchange(
              "https://secure.snd.payu.com/api/v2_1/shops/" + shopId,
              HttpMethod.GET,
              request,
              String.class);
    } catch (HttpClientErrorException.Unauthorized e) {
      response =
          changeBearerAuth(
              headers,
              null,
              "https://secure.snd.payu.com/api/v2_1/shops/" + shopId,
              HttpMethod.GET);
    }
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      return null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(response.getBody());
    return jsonNode.get("balance").get("available").asText();
  }

  public void cancelPayout(String orderId) {
    reviewService.deleteSelectReview(orderId);
    logger.error("cancel Payout");
  }

  public ResponseEntity<String> changeBearerAuth(
      HttpHeaders headers, Map<String, Object> body, String link, HttpMethod method)
      throws JsonProcessingException {
    setToken();
    headers.setBearerAuth(token);
    HttpEntity<?> updateRequest;
    if (body == null) {
      updateRequest = new HttpEntity<>(headers);

    } else {
      updateRequest = new HttpEntity<>(body, headers);
    }
    return restTemplate.exchange(link, method, updateRequest, String.class);
  }

  public String getAdditionalDescription(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readTree(requestBody).get("order").get("additionalDescription").asText();
  }

  public boolean setCompleted(String orderId) throws JsonProcessingException {
    try {
      if (!semaphore.tryAcquire(10, TimeUnit.MINUTES)) {
        return false;
      }
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(token);
      HttpEntity<Object> request = new HttpEntity<>(headers);
      ResponseEntity<String> response;
      try {
        response =
            restTemplate.exchange(
                "https://secure.snd.payu.com/api/v2_1/orders/" + orderId + "/captures",
                HttpMethod.POST,
                request,
                String.class);
      } catch (HttpClientErrorException.Unauthorized e) {
        response =
            changeBearerAuth(
                headers,
                null,
                "https://secure.snd.payu.com/api/v2_1/orders/" + orderId + "/captures",
                HttpMethod.POST);
      }
      if (!response.getStatusCode().equals(HttpStatus.OK)) {
        return false;
      }
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper
          .readTree(response.getBody())
          .get("status")
          .get("statusCode")
          .asText()
          .equals("SUCCESS");
    } catch (InterruptedException e) {
      return false;
    }
  }

  public boolean isRefund(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readTree(requestBody).has("refund");
  }
}
