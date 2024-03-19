package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;

@Service
public class OpinionService {

  private final ReviewService reviewService;

  private final TipService tipService;

  private final Logger logger = LoggerFactory.getLogger(OpinionService.class);

  public OpinionService(ReviewService reviewService, TipService tipService) {
    this.reviewService = reviewService;
    this.tipService = tipService;
  }

  public ResponseEntity<String> addOpinion(OpinionPostDTO opinionPostDTO, String ip) {
    try {
      ResponseEntity<String> response = tipService.createPayment(opinionPostDTO, ip);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(response.getBody());
      if (!reviewService.addReview(opinionPostDTO, jsonNode.get("orderId").asText())) {
        return null;
      } else {
        return response;
      }
    } catch (Exception e) {
      logger.error("addOpinion", e);
      return null;
    }
  }
}
