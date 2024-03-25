package umk.mat.pajda.ProjektZespolowy.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.services.OpinionService;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;
import umk.mat.pajda.ProjektZespolowy.services.TipService;

@RequestMapping("/opinion")
@RestController
@Tag(
    name = "Opinion Endpoints",
    description = "Controller for handling requests related to add opinion")
public class OpinionController {

  @Value("${profile}")
  private String profile;

  @Value("${FIXEDSALT_IPHASH}")
  private String fixedSalt;

  private final OpinionService opinionService;

  private final ReviewService reviewService;

  private final TipService tipService;

  private final Logger logger = LoggerFactory.getLogger(OpinionController.class);

  public OpinionController(OpinionService opinionService, ReviewService reviewService, TipService tipService) {
    this.opinionService = opinionService;
    this.reviewService = reviewService;
    this.tipService = tipService;
  }

  @PostMapping
  @Operation(
      summary = "POST - Add \"new Opinion\"",
      description = "Following endpoint adds new Opinion")
  public ResponseEntity<String> addNewOpinion(
      @Valid @RequestBody OpinionPostDTO opinionPostDTO, BindingResult bindingResult) throws JsonProcessingException {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("Validation failed: " + bindingResult.getAllErrors());
    }
    String exchangeRate = "1";
    int lastAmount = opinionPostDTO.getAmount();
    int amount = lastAmount;
    String currency = opinionPostDTO.getCurrency();
    if(!currency.equals("PLN"))
    {
      exchangeRate = tipService.getExchangeRate(opinionPostDTO.getCurrency());
      if (exchangeRate == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("exchangeRate - not found");
      }
      lastAmount = Math.round(amount * Float.parseFloat(exchangeRate));
    }
    if(lastAmount<80){
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
              .body("Validation failed: amount is below min");
    }
    String ip = opinionPostDTO.getHashRevID();
    opinionPostDTO.setHashRevID(BCrypt.hashpw(opinionPostDTO.getHashRevID(), fixedSalt));
    if ("prod".equals(profile) && !reviewService.validateTime(opinionPostDTO)) {

      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .body("adding failed - too many requests wait 10 minutes");
    }
    ResponseEntity<String> response = opinionService.addOpinion(opinionPostDTO, ip, lastAmount, exchangeRate);
    if (response == null) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("adding failed");
    } else {
      return response;
    }
  }
}
