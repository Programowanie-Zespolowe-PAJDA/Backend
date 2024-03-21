package umk.mat.pajda.ProjektZespolowy.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;
import umk.mat.pajda.ProjektZespolowy.services.TipService;

@RestController
@RequestMapping("/tip")
@Tag(name = "Tip Endpoints", description = "Controller for handling requests related to add tip")
public class TipController {

  private final TipService tipService;
  private final ReviewService reviewService;

  private final Logger logger = LoggerFactory.getLogger(TipController.class);

  public TipController(TipService tipService, ReviewService reviewService) {
    this.tipService = tipService;
    this.reviewService = reviewService;
  }

  @PostMapping
  public void addTip(
      @RequestBody String requestBody, @RequestHeader("OpenPayu-Signature") String header)
      throws NoSuchAlgorithmException, JsonProcessingException {
    logger.info(requestBody);
    logger.info(header);
    if (tipService.verifyNotification(requestBody, header)) {
      String status = tipService.getStatus(requestBody);
      logger.info(status);
      if (status.equals("CANCELED")) {
        if (!reviewService.deleteSelectReview(tipService.getOrderId(requestBody))) {
          logger.error("deleting failed");
        }
      } else if (status.equals("COMPLETED")) {
        logger.info(requestBody);
        tipService.makePayout();
      }
    }
  }
}
