package umk.mat.pajda.ProjektZespolowy.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;

@Service
public class OpinionService {

  private final ReviewService reviewService;

  private final TipService tipService;

  private final Logger logger = LoggerFactory.getLogger(OpinionService.class);

  public OpinionService(ReviewService reviewService, TipService tipService) {
    this.reviewService = reviewService;
    this.tipService = tipService;
  }

  public ResponseEntity<String> addOpinion(
      OpinionPostDTO opinionPostDTO, String ip, int lastAmount, String exchangeRate) {
    try {
      ResponseEntity<String> response =
          tipService.createPayment(opinionPostDTO, ip, lastAmount, exchangeRate);
      if (response == null) {
        return null;
      }
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

  public List<OpinionGetDTO> getOpinions(String email) {
    List<OpinionGetDTO> list = new ArrayList<>();
    for (Review review : reviewService.getAllReviewsByEmail(email)) {
      Tip tip = review.getTip();
      list.add(
          new OpinionGetDTO(
              review.getRating(),
              tip.getRealAmount(),
              tip.getCurrency(),
              review.getComment(),
              review.getClientName()));
    }
    return list;
  }
}
