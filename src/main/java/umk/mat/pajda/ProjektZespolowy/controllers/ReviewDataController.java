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
@Tag(
    name = "Review Endpoints",
    description = "Controller for handling requests related to add/del/patch/read reviews")
public class ReviewDataController {

  private final ReviewService reviewService;

  @Autowired
  public ReviewDataController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PatchMapping("/{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "PATCH - modify \"Review\"",
      description = "Following endpoint modifies a Review")
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
      summary = "DELETE - delete \"Review\"",
      description = "Following endpoint deletes a Review of id")
  public ResponseEntity<String> delReview(@PathVariable String id) {
    if (reviewService.deleteSelectReview(id)) {
      return ResponseEntity.status(HttpStatus.OK).body("delete successful");
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("delete failed");
    }
  }

  @GetMapping
  @Operation(
      summary = "GET - get all \"Review\"",
      description = "Following endpoint returns a Review")
  public ResponseEntity<List<ReviewGetDTO>> readReview() {
    return ResponseEntity.status(HttpStatus.OK).body(reviewService.getAllReviews());
  }

  @GetMapping("/owner")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "GET - get all owner \"Review\"",
      description = "Following endpoint returns all owner reviews")
  public ResponseEntity<List<ReviewGetDTO>> readReview(
      @AuthenticationPrincipal UserDetails userDetails) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(reviewService.getAllReviews(userDetails.getUsername()));
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "GET - get \"Review\"",
      description = "Following endpoint returns a Review of id")
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
      summary = "GET - get owner \"Review\"",
      description = "Following endpoint returns a owner Review of id")
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
      summary = "GET - get avg rating of all \"Review\"",
      description = "Following endpoint returns avg rating of all owner Reviews")
  public ResponseEntity<ReviewAvgRatingGetDTO> getAvgRating(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(reviewService.getAvgRatingOfReview(userDetails.getUsername()));
  }

  @GetMapping("/numberOfEachRating")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "GET - get number of each rating of all \"Review\"",
      description = "Following endpoint returns number of each rating of all owner Reviews " +
              "in form of a array from 0 to 10 rating")
  public ResponseEntity<List<Integer>> getNumberOfEachRatings(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(reviewService.getNumberOfEachRatings(userDetails.getUsername()));
  }
}
