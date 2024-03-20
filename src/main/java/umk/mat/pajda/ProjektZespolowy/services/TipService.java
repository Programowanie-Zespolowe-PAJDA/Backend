package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
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

  @Value("${profile}")
  private String profile;

  public TipService(TipConverter tipConverter, TipRepository tipRepository) {
    this.tipConverter = tipConverter;
    this.tipRepository = tipRepository;
    this.restTemplate = new RestTemplate();
  }

  public boolean verifyNotification(String requestBody, HttpHeaders headers) {
    headers.forEach(
        (key, value) -> {
          logger.info(String.format("Header '%s' = %s", key, value));
        });
    return true;
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
    headers.setBearerAuth(getToken());
    headers.setContentType(MediaType.APPLICATION_JSON);
    Integer amount = opinionPostDTO.getAmount();
    Map<String, Object> products = new HashMap<>();
    products.put("name", "Napiwek");
    products.put("unitPrice", String.valueOf(amount));
    products.put("quantity", "1");
    Map<String, Object> body = new HashMap<>();
    body.put("customerIp", ip);
    body.put("merchantPosId", clientId);
    body.put("description", opinionPostDTO.getUserID().toString());
    body.put("currencyCode", opinionPostDTO.getCurrency());
    body.put("totalAmount", String.valueOf(amount));
    if ("prod".equals(profile)) {
      body.put("notifyUrl", "https://enapiwek-api.onrender.com/tip");
      body.put("continueUrl", "https://enapiwek.onrender.com/thankyou");
    } else {
      body.put("continueUrl", "http://localhost:5173/thankyou");
    }
    ObjectMapper objectMapper = new ObjectMapper();
    body.put("products", new Object[] {products});
    HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
    return restTemplate.exchange(
        "https://secure.snd.payu.com/api/v2_1/orders", HttpMethod.POST, request, String.class);
  }

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
}
