package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewAvgRatingGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewPatchDTO;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;

@RequestMapping("/review")
@RestController
@Tag(name = "ReviewDataController", description = "Kontroler do obsługiwania recenzji")
public class ReviewDataController {

  private final ReviewService reviewService;

  @Autowired
  public ReviewDataController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PatchMapping("/{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Modyfikowanie recenzji",
      description =
          "Ten endpoint odpowiada za modyfikacje istniejącej recenzji. Sprawdza takie rzeczy jak: \n"
              + "- walidacje\n"
              + "- czy istnieje recenzja o takim id\n\n"
              + "Jeżeli któraś z tych rzeczy zakończy się błędem, to nie jest modyfikowana recenzja.\n"
              + "W każdym przypadku dostajemy stosowaną informację wraz z odpowiednim statusem.\n")
  public ResponseEntity<String> modReview(
      @Valid @RequestBody ReviewPatchDTO reviewPatchDTO,
      BindingResult bindingResult,
      @PathVariable String id) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("Validation failed: " + bindingResult.getAllErrors());
    }

    if (reviewService.getReview(id) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("modifying failed - no review with such id");
    }

    if (reviewService.patchSelectReview(reviewPatchDTO, id)) {
      return ResponseEntity.status(HttpStatus.OK).body("modifying successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("modifying failed");
    }
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Usuwanie recenzji",
      description =
          "Ten endpoint odpowiada za usuwanie istniejącej recenzji. "
              + "Wysyłany jest odpowiednia informacja wraz z statusem.")
  public ResponseEntity<String> delReview(@PathVariable String id) {
    if (reviewService.deleteSelectReview(id)) {
      return ResponseEntity.status(HttpStatus.OK).body("delete successful");
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("delete failed");
    }
  }

  @GetMapping
  @Operation(
      summary = "Zwracanie istniejących recenzji",
      description = "Ten endpoint zwraca listę wszystkich istniejących recenzji.")
  public ResponseEntity<List<ReviewGetDTO>> readReview() {
    return ResponseEntity.status(HttpStatus.OK).body(reviewService.getAllReviews());
  }

  @GetMapping("/owner")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Zwracanie istniejących recenzji zalogowanego kelenera",
      description = "Ten endpoint zwraca listę wszystkich recenzji dla zalogowanego kelenera.")
  public ResponseEntity<List<ReviewGetDTO>> readReview(
      @AuthenticationPrincipal UserDetails userDetails) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(reviewService.getAllReviews(userDetails.getUsername()));
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Zwracanie danej recenzji",
      description =
          "Ten endpoint zwraca recenzje o danym id. Jeżeli takiej nie ma, to "
              + "zostaje wysłany odpowiedni komunikat wraz z statusem.")
  public ResponseEntity<ReviewGetDTO> readReview(@PathVariable String id) {
    ReviewGetDTO reviewGetDTO = null;
    reviewGetDTO = reviewService.getReview(id);
    if (reviewGetDTO != null) {
      return ResponseEntity.status(HttpStatus.OK).body(reviewGetDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @GetMapping("/owner/{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Zwracanie danej recenzji zalogowanego kelnera",
      description =
          "Ten endpoint zwraca recenzje o danym id dla zalogowanego kelnera. "
              + "Jeżeli takiej nie ma, to zostaje wysłany odpowiedni komunikat wraz z statusem.")
  public ResponseEntity<ReviewGetDTO> readReview(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable String id) {
    ReviewGetDTO reviewGetDTO = null;
    reviewGetDTO = reviewService.getReview(id, userDetails.getUsername());
    if (reviewGetDTO != null) {
      return ResponseEntity.status(HttpStatus.OK).body(reviewGetDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @GetMapping("/avgRating")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Zwracanie średniej oceny",
      description = "Ten endpoint zwraca średnią ocenę dla zalogowanego kelnera.")
  public ResponseEntity<ReviewAvgRatingGetDTO> getAvgRating(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(reviewService.getAvgRatingOfReview(userDetails.getUsername()));
  }

  @GetMapping("/numberOfEachRating")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Zwracanie ilość ocen",
      description =
          "Ten endpoint zwraca listę ilości ocen od wartości 0 do wartości 10 dla zalogowanego kelnera.")
  public ResponseEntity<List<Integer>> getNumberOfEachRatings(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(reviewService.getNumberOfEachRatings(userDetails.getUsername()));
  }
}
