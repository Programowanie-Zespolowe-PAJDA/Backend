package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import umk.mat.pajda.ProjektZespolowy.services.TipService;

@RestController
@RequestMapping("/tip")
@Tag(name = "Tip Endpoints", description = "Controller for handling requests related to add tip")
public class TipController {

  private final TipService tipService;

  private final Logger logger = LoggerFactory.getLogger(TipController.class);

  public TipController(TipService tipService) {
    this.tipService = tipService;
  }

  @PostMapping
  public void addTip(
      @RequestBody String requestBody, @RequestHeader("openpayu-signature") String header) {

    logger.info("header");
    // tipService.verifyNotification(requestBody, headers);
  }
}
