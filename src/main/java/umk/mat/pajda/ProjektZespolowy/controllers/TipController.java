package umk.mat.pajda.ProjektZespolowy.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;
import umk.mat.pajda.ProjektZespolowy.services.TipService;

@RestController
@RequestMapping("/tip")
@Tag(name = "Tip Endpoints", description = "Controller for handling requests related to add tip")
public class TipController {

  private final TipService tipService;
  private final ReviewService reviewService;



  public TipController(TipService tipService, ReviewService reviewService) {
    this.tipService = tipService;
    this.reviewService = reviewService;
  }
  @PostMapping
  public void addTip(
      @RequestBody String requestBody, @RequestHeader("OpenPayu-Signature") String header)
      throws NoSuchAlgorithmException, JsonProcessingException {
    if (!tipService.verifyNotification(requestBody, header)) {
      String status = tipService.getStatus(requestBody);
      String orderId = tipService.getOrderId(requestBody);
      if (status.equals("CANCELED")) {
          reviewService.deleteSelectReview(orderId);
      } else if (status.equals("COMPLETED")) {
        String amount = tipService.getAmount(requestBody);
        String currency = tipService.getCurrency(requestBody);
        String token = tipService.getToken();
        if(token==null)
        {
          tipService.cancelPayout(orderId, null);
          return;
        }
        String paidWith = tipService.getPaidWith(orderId, token);
        if(paidWith==null){
          tipService.cancelPayout(orderId, token);
          return;
        }
        List<String> list = tipService.getRealAmounts(amount, currency, paidWith, token, tipService.getAdditionalDescription(requestBody));
        if(list == null){
          tipService.cancelPayout(orderId, token);
          return;
        }
        String payoutId = tipService.makePayout(orderId, list.get(1), token);
        if(payoutId!=null)
        {
          if(tipService.addTip(payoutId, orderId, list.get(0), paidWith, currency)){
              if(!reviewService.setEnabled(orderId)){
                reviewService.deleteSelectReview(orderId);
              }
          }
          else {
            reviewService.deleteSelectReview(orderId);
          }
        }else{
          tipService.cancelPayout(orderId, token);
        }
      }
    }
  }
}
