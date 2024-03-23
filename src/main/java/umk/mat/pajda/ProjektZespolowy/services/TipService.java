package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.TipRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Service
public class TipService {
  private final Logger logger = LoggerFactory.getLogger(TipService.class);
  private final TipConverter tipConverter;
  private final TipRepository tipRepository;

  private final UserRepository userRepository;

  private final ReviewService reviewService;

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

  public TipService(
      TipConverter tipConverter,
      TipRepository tipRepository,
      UserRepository userRepository,
      ReviewService reviewService,
      UserService userService) {
    this.tipConverter = tipConverter;
    this.tipRepository = tipRepository;
    this.userRepository = userRepository;
    this.reviewService = reviewService;
    this.userService = userService;
    this.restTemplate = new RestTemplate();
  }

  public String makePayout(String orderId, String realAmount, String token)
      throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Map<String, Object> body = new HashMap<>();
    body.put("shopId", shopId);
    Map<String, Object> payout = new HashMap<>();
    logger.info(realAmount);
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
    ResponseEntity<String> response =
        restTemplate.exchange(
            "https://secure.snd.payu.com/api/v2_1/payouts", HttpMethod.POST, request, String.class);
    if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
      return null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(response.getBody());
    if (jsonNode.get("status").get("statusCode").asText().equals("SUCCESS")) {
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

  public String getToken() throws JsonProcessingException {
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
      return null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(tokenResponse.getBody());
    return jsonNode.get("access_token").asText();
  }

  public ResponseEntity<String> createPayment(OpinionPostDTO opinionPostDTO, String ip)
      throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    String token = getToken();
    if (token == null) {
      return null;
    }
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Integer amount = opinionPostDTO.getAmount();
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
    }
    String currency = opinionPostDTO.getCurrency();
    if (!currency.equals("PLN")) {
      String exchangeRate = getExchangeRate(token, currency);
      if (exchangeRate == null) {
        return null;
      }
      int lastAmount = Math.round(amount * Float.parseFloat(exchangeRate));
      body.put("totalAmount", String.valueOf(lastAmount));
      products.put("unitPrice", String.valueOf(lastAmount));
      body.put("additionalDescription", exchangeRate);
    } else {
      products.put("unitPrice", String.valueOf(amount));
      body.put("totalAmount", String.valueOf(amount));
    }
    body.put("description", opinionPostDTO.getCurrency());
    ObjectMapper objectMapper = new ObjectMapper();
    body.put("products", new Object[] {products});
    HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            "https://secure.snd.payu.com/api/v2_1/orders", HttpMethod.POST, request, String.class);
    if (!response.getStatusCode().equals(HttpStatus.FOUND)) {
      return null;
    }
    return response;
  }

  public String getExchangeRate(String token, String currency) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<Object> request = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            "https://secure.snd.payu.com/api/v2_1/mcp-partners/6283a549-8b1a-430d-8a62-eea64327440e/fx-table",
            HttpMethod.GET,
            request,
            String.class);
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
      String payoutId, String orderId, String realAmount, String paidWith, String currency) {
    try {
      String nameOfPaidWith =
          switch (paidWith) {
            case "blik" -> "BLIK";
            case "c" -> "KARTA_PŁATNICZA";
            case "p", "o", "m" -> "PRZELEW";
            case "dpkl" -> "KLARNA";
            default -> null;
          };
      tipRepository.save(
          tipConverter.createEntity(payoutId, orderId, realAmount, nameOfPaidWith, currency));
    } catch (Exception e) {
      logger.error("addTip", e);
      return false;
    }
    return true;
  }

  public String getPaidWith(String orderId, String token) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    HttpEntity<Object> request = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            "https://secure.snd.payu.com/api/v2_1/orders/" + orderId + "/transactions",
            HttpMethod.GET,
            request,
            String.class);
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      return null;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode node = objectMapper.readTree(response.getBody());
    for (JsonNode jsonNode : node.get("transactions")) {
      return jsonNode.get("payMethod").get("value").asText();
    }
    return null;
  }

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public boolean makeRefund(String token, String orderId) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Map<String, Object> body = new HashMap<>();
    Map<String, String> description = new HashMap<>();
    description.put("description", "Refund");
    body.put("refund", description);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            "https://secure.snd.payu.com/api/v2_1/orders/" + orderId + "/refunds",
            HttpMethod.POST,
            request,
            String.class);
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      return false;
    }
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(response.getBody());
    return jsonNode.get("status").get("statusCode").asText().equals("SUCCESS");
  }

  public String getCurrency(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readTree(requestBody).get("order").get("description").asText();
  }

  public String getAmount(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readTree(requestBody).get("order").get("totalAmount").asText();
  }

  public List<String> getRealAmounts(
      String totalAmount, String currency, String paidWith, String token, String exchangeRate)
      throws JsonProcessingException {
    int amount = Integer.parseInt(totalAmount);
    float commissionFee;
    switch (paidWith) {
      case "blik" -> {
        commissionFee = (float) (amount * 0.034);
        amount = amount - 70 - Math.round(commissionFee);
      }
      case "c" -> {
        commissionFee = (float) (amount * 0.031);
        amount = amount - 40 - Math.round(commissionFee);
      }
      case "p", "o" -> {
        commissionFee = (float) (amount * 0.029);
        amount = amount - 30 - Math.round(commissionFee);
      }
      case "m" -> {
        commissionFee = (float) (amount * 0.028);
        amount = amount - 50 - Math.round(commissionFee);
      }
      case "dpkl" -> {
        commissionFee = (float) (amount * 0.025);
        amount = amount - 35 - Math.round(commissionFee);
      }
      default -> {}
    }
    if (currency.equals("PLN")) {
      return List.of(String.valueOf(amount), String.valueOf(amount));
    } else {
      int lastAmount = Math.round(amount / Float.parseFloat(exchangeRate));
      return List.of(String.valueOf(lastAmount), String.valueOf(amount));
    }
  }

  public void cancelPayout(String orderId, String token) throws JsonProcessingException {
    reviewService.deleteSelectReview(orderId);
    if (token != null && !makeRefund(token, orderId)) {
      logger.error("makeRefund - failed");
      if ("prod".equals(profile)) {
        try {
          emailService.send(
              userRepository.findByMail("enapiwek@gmail.com").get(),
              "Refund",
              "refund failed for order: " + orderId);
        } catch (Exception e) {
          logger.error("sendMail - failed", e);
        }
      }
    }
  }

  public String getAdditionalDescription(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readTree(requestBody).get("order").get("additionalDescription").asText();
  }
}
