package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.services.OpinionService;

@RequestMapping("/opinion")
@RestController
@Tag(
    name = "Opinion Endpoints",
    description = "Controller for handling requests related to add opinion")
public class OpinionController {

  private final OpinionService opinionService;

  private final Logger logger = LoggerFactory.getLogger(OpinionController.class);

  public OpinionController(OpinionService opinionService) {
    this.opinionService = opinionService;
  }

  @PostMapping
  @Operation(
      summary = "POST - Add \"new Opinion\"",
      description = "Following endpoint adds new Opinion")
  public ResponseEntity<String> addNewOpinion(
      @Valid @RequestBody OpinionPostDTO opinionPostDTO, BindingResult bindingResult) {
    logger.info(opinionPostDTO.getComment(), opinionPostDTO.getAmount());
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("Validation failed: " + bindingResult.getAllErrors());
    }
    return opinionService.addOpinion(opinionPostDTO);
  }
}
