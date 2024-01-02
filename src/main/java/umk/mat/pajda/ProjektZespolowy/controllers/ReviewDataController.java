package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewDTO;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;

@CrossOrigin
@RequestMapping("/review")
@RestController
@Tag(
    name = "Opinion Endpoints",
    description = "Controller for handling requests related to adding/del/patch/read opinion")
public class ReviewDataController {

  private final ReviewService reviewService;

  @Autowired
  public ReviewDataController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  // TODO - validate if monetary transaction happened , validate if there were only one request in
  // 30-60 min
  @PostMapping("/add")
  @Operation(
      summary = "POST - Add \"new Review\"",
      description = "Following endpoint adds new Review")
  public ResponseEntity<String> addNewReview(@RequestBody ReviewDTO reviewDTO) {
    if (reviewService.addReview(reviewDTO)) {
      return ResponseEntity.status(HttpStatus.CREATED).body("adding successful");
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("adding failed");
    }
  }

  @PatchMapping("/patch")
  @Operation(
      summary = "PATCH - modify \"Review\"",
      description = "Following endpoint modify a Review")
  public ResponseEntity<String> modReview(@RequestBody ReviewDTO reviewDTO) {
    if (reviewService.patchSelectReview(reviewDTO)) {
      return ResponseEntity.status(HttpStatus.OK).body("modifying successful");
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("modifying failed");
    }
  }

  @DeleteMapping("/del/{id}")
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

  @GetMapping("/read")
  @Operation(
      summary = "Get - get all \"Review\"",
      description = "Following endpoint returns a Review")
  public ResponseEntity<List<ReviewDTO>> readReview() {
    return ResponseEntity.status(HttpStatus.OK).body(reviewService.getAllReviews());
  }

  @GetMapping("/read/{id}")
  @Operation(summary = "Get - get \"Review\"", description = "Following endpoint returns a Review")
  public ResponseEntity<ReviewDTO> readReview(@PathVariable int id) {
    return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(id));
  }
}
