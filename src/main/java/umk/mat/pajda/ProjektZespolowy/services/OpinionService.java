package umk.mat.pajda.ProjektZespolowy.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;

@Service
public class OpinionService {

  private final ReviewService reviewService;

  @Autowired(required = false)
  private TipService tipService;

  private final Logger logger = LoggerFactory.getLogger(OpinionService.class);

  public OpinionService(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  public ResponseEntity<String> addOpinion(OpinionPostDTO opinionPostDTO, String ip) {
    try {
      reviewService.addReview(opinionPostDTO);
      return tipService.createPayment(opinionPostDTO, ip);
    } catch (Exception e) {
      logger.error("addOpinion", e);
      return null;
    }
  }
}
