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
      throws NoSuchAlgorithmException, JsonProcessingException, InterruptedException {

    if (tipService.verifyNotification(requestBody, header)) {
      if (tipService.isRefund(requestBody)) {
        ResponseEntity.status(HttpStatus.OK).body("correct notification");
      }
      String status = tipService.getStatus(requestBody);
      String orderId = tipService.getOrderId(requestBody);
      if (status.equals(reviewService.getReviewById(orderId).getStatus())) {
        return ResponseEntity.status(HttpStatus.OK).body("correct notification");
      }
      if (status.equals("WAITING_FOR_CONFIRMATION")) {
        if (!reviewService.setStatus(orderId, "WAITING_FOR_CONFIRMATION")) {
          reviewService.deleteSelectReview(orderId);
        }
        if (!tipService.setCompleted(orderId)) {
          reviewService.deleteSelectReview(orderId);
        }
        return ResponseEntity.status(HttpStatus.OK).body("correct notification");

      } else if (status.equals("CANCELED")) {
        reviewService.deleteSelectReview(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("correct notification");
      } else if (status.equals("COMPLETED")) {
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
          if (!tipService.addTip(payoutId, orderId, lastAmount, paidWith, currency, exchangeRate)) {
            reviewService.deleteSelectReview(orderId);
          }
        } else {
          reviewService.deleteSelectReview(orderId);
        }
        return ResponseEntity.status(HttpStatus.OK).body("correct notification");
      }
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("bad verification");
  }
}
