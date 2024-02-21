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
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewPatchPostDTO;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;

// TODO - if CrossOrigin is fixed remove CrossOrigin annotation

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

  // TODO - validate if monetary transaction happened
  // TODO - validate if there were only one request in 30-60 min
  @PostMapping
  @Operation(
      summary = "POST - Add \"new Review\"",
      description = "Following endpoint adds new Review")
  public ResponseEntity<String> addNewReview(
      @Valid @RequestBody ReviewPatchPostDTO reviewPatchPostDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("Validation failed: " + bindingResult.getAllErrors());
    }
    if (reviewService.addReview(reviewPatchPostDTO)) {
      return ResponseEntity.status(HttpStatus.CREATED).body("adding successful");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("adding failed");
    }
  }

  @PatchMapping("/{id}")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "PATCH - modify \"Review\"",
      description = "Following endpoint modifies a Review")
  public ResponseEntity<String> modReview(
      @Valid @RequestBody ReviewPatchPostDTO reviewPatchPostDTO,
      BindingResult bindingResult,
      @PathVariable int id) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
          .body("Validation failed: " + bindingResult.getAllErrors());
    }

    if (reviewService.getReview(id) == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("modifying failed - no review with such id");
    }

    if (reviewService.patchSelectReview(reviewPatchPostDTO, id)) {
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
  public ResponseEntity<String> delReview(@PathVariable int id) {
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
  public ResponseEntity<ReviewGetDTO> readReview(@PathVariable int id) {
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
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable int id) {
    ReviewGetDTO reviewGetDTO = null;
    reviewGetDTO = reviewService.getReview(id, userDetails.getUsername());
    if (reviewGetDTO != null) {
      return ResponseEntity.status(HttpStatus.OK).body(reviewGetDTO);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }
}
