package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umk.mat.pajda.ProjektZespolowy.DTO.TipStatisticsGetDTO;
import umk.mat.pajda.ProjektZespolowy.services.TipService;

@RequestMapping("/tip")
@RestController
@Tag(name = "Tip Endpoints", description = "Controller for handling requests related to get Tip")
public class TipController {

  private final TipService tipService;

  @Autowired
  public TipController(TipService tipService) {
    this.tipService = tipService;
  }

  @GetMapping("/stats")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "GET - get \"tip statistics\"",
      description = "Following endpoint returns tip statistics of user")
  public ResponseEntity<TipStatisticsGetDTO> getTipStatistics(
      @AuthenticationPrincipal UserDetails userDetails) {
    TipStatisticsGetDTO returnData = tipService.getStatistics(userDetails.getUsername());
    if (returnData != null) {
      return ResponseEntity.status(HttpStatus.OK).body(returnData);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
    }
  }
}
