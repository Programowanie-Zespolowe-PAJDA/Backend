package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.misc.TipConverter;
import umk.mat.pajda.ProjektZespolowy.repository.TipRepository;

@Service
public class TipService {
  private final Logger logger = LoggerFactory.getLogger(TipService.class);
  private final TipConverter tipConverter;
  private final TipRepository tipRepository;

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

  public TipService(TipConverter tipConverter, TipRepository tipRepository) {
    this.tipConverter = tipConverter;
    this.tipRepository = tipRepository;
    this.restTemplate = new RestTemplate();
  }

  public boolean makePayout() {
    /*
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(getToken());
    headers.setContentType(MediaType.APPLICATION_JSON);
    Map<String, Object> body = new HashMap<>();
    body.put("shopId", shopId);
    Map<String, Object> payout = new HashMap<>();
    payout.put("amount",);\
     */
    return true;
  }

  public String getStatus(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(requestBody);
    return jsonNode.get("status").asText();
  }

  public String getOrderId(String requestBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(requestBody);
    return jsonNode.get("orderId").asText();
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
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(tokenResponse.getBody());
    return jsonNode.get("access_token").asText();
  }

  public ResponseEntity<String> createPayment(OpinionPostDTO opinionPostDTO, String ip)
      throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    String token = getToken();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Integer amount = opinionPostDTO.getAmount();
    Map<String, Object> products = new HashMap<>();
    products.put("name", "Napiwek");
    products.put("quantity", "1");
    Map<String, Object> body = new HashMap<>();
    body.put("customerIp", ip);
    body.put("merchantPosId", clientId);
    body.put("description", opinionPostDTO.getUserID().toString());
    body.put("currencyCode", "PLN");
    if ("prod".equals(profile)) {
      body.put("notifyUrl", "https://enapiwek-api.onrender.com/tip");
      body.put("continueUrl", "https://enapiwek.onrender.com/thankyou");
    } else {
      body.put("continueUrl", "http://localhost:5173/thankyou");
    }
    String currency = opinionPostDTO.getCurrency();
    if (!currency.equals("PLN")) {
      List<String> list = getExchangeRate(token, currency);
      Map<String, Object> mcpData = new HashMap<>();
      mcpData.put("mcpFxTableId", list.get(0));
      mcpData.put("mcpCurrency", currency);
      mcpData.put("mcpRate", list.get(1));
      mcpData.put("mcpAmount", String.valueOf(amount));
      mcpData.put("mcpPartnerId", "6283a549-8b1a-430d-8a62-eea64327440e");
      int lastAmount = Math.round(amount * Float.parseFloat(list.get(1)));
      logger.info(String.valueOf(lastAmount));
      body.put("totalAmount", lastAmount);
      products.put("unitPrice", lastAmount);
      body.put("mcpData", mcpData);
    } else {
      products.put("unitPrice", String.valueOf(amount));
      body.put("totalAmount", String.valueOf(amount));
    }
    ObjectMapper objectMapper = new ObjectMapper();
    body.put("products", new Object[] {products});
    HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
    logger.info(request.getBody());
    return restTemplate.exchange(
        "https://secure.snd.payu.com/api/v2_1/orders", HttpMethod.POST, request, String.class);
  }

  public List<String> getExchangeRate(String token, String currency)
      throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<Object> request = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response =
        restTemplate.exchange(
            "https://secure.snd.payu.com/api/v2_1/mcp-partners/6283a549-8b1a-430d-8a62-eea64327440e/fx-table",
            HttpMethod.GET,
            request,
            String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(response.getBody());
    List<String> list = new ArrayList<>();
    list.add(jsonNode.get("id").asText());
    for (JsonNode node : jsonNode.get("currencyPairs")) {
      logger.info(node.asText());
      if (node.get("baseCurrency").asText().equals(currency)
          && node.get("termCurrency").asText().equals("PLN")) {
        list.add(node.get("exchangeRate").asText());
        break;
      }
    }
    return list;
  }

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
}
