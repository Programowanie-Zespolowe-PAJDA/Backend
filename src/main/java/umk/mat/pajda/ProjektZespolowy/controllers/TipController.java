package umk.mat.pajda.ProjektZespolowy.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<String> addTip(
      @RequestBody String requestBody, @RequestHeader("OpenPayu-Signature") String header)
      throws NoSuchAlgorithmException, JsonProcessingException {

    if (tipService.verifyNotification(requestBody, header)) {
      String status = tipService.getStatus(requestBody);
      String orderId = tipService.getOrderId(requestBody);
      String orderStatus = reviewService.getReviewById(orderId).getStatus();
      if ("COMPLETED".equals(orderStatus)) {
        return ResponseEntity.status(HttpStatus.OK).body("correct notification");
      } else if (status.equals(orderStatus)) {
        return ResponseEntity.status(HttpStatus.OK).body("correct notification");
      } else if (status.equals("PENDING")) {
        return ResponseEntity.status(HttpStatus.OK).body("correct notification");
      }
      switch (status) {
        case "WAITING_FOR_CONFIRMATION" -> {
          if (!reviewService.setStatus(orderId, "WAITING_FOR_CONFIRMATION")) {
            reviewService.deleteSelectReview(orderId);
          }
          if (!tipService.setCompleted(orderId)) {
            reviewService.deleteSelectReview(orderId);
          }
        }
        case "CANCELED" -> reviewService.deleteSelectReview(orderId);
        case "COMPLETED" -> {
          if (!reviewService.setStatus(orderId, "COMPLETED")) {
            reviewService.deleteSelectReview(orderId);
          }
          String currency = tipService.getCurrency(requestBody);
          String paidWith = tipService.getPaidWith(orderId);
          if (paidWith == null) {
            reviewService.deleteSelectReview(orderId);
          }
          String exchangeRate = tipService.getAdditionalDescription(requestBody);
          String lastAmount = tipService.getRealAmount();
          if (lastAmount == null) {
            reviewService.deleteSelectReview(orderId);
          }
          String payoutId = tipService.makePayout(orderId, lastAmount);
          if (payoutId != null) {
            if (!tipService.addTip(
                payoutId, orderId, lastAmount, paidWith, currency, exchangeRate)) {
              reviewService.deleteSelectReview(orderId);
            }
          } else {
            reviewService.deleteSelectReview(orderId);
          }
        }
        default -> logger.info(status);
      }
      return ResponseEntity.status(HttpStatus.OK).body("correct notification");
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("bad verification");
  }
}
