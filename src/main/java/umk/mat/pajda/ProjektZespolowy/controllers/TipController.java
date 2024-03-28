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
      logger.info(status);
      if (status.equals("CANCELED")) {
        reviewService.deleteSelectReview(orderId);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("bad status");
      } else if (status.equals("COMPLETED")) {
        String amount = tipService.getAmount(requestBody);
        String currency = tipService.getCurrency(requestBody);
        String paidWith = tipService.getPaidWith(orderId);
        if (paidWith == null) {

          tipService.cancelPayout(orderId);
          return ResponseEntity.status(HttpStatus.NO_CONTENT).body("error with paidWith");
        }
        String lastAmount = tipService.getRealAmount(amount, paidWith);
        String payoutId = tipService.makePayout(orderId, lastAmount);
        if (payoutId != null) {
          if (tipService.addTip(payoutId, orderId, lastAmount, paidWith, currency)) {
            if (!reviewService.setEnabled(orderId)) {
              reviewService.deleteSelectReview(orderId);
              return ResponseEntity.status(HttpStatus.NO_CONTENT).body("error with setEnabled");
            } else {
              return ResponseEntity.status(HttpStatus.CREATED).body("adding successful");
            }
          } else {
            reviewService.deleteSelectReview(orderId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("error with addTip");
          }
        } else {
          tipService.cancelPayout(orderId);
          return ResponseEntity.status(HttpStatus.NO_CONTENT).body("error with makePayout");
        }
      }
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("bad verification");
  }
}
