package umk.mat.pajda.ProjektZespolowy.Services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewPatchPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;

@SpringBootTest
public class ReviewService_IntTestOfHashRevID {

  private final String fixedSalt = "$2a$10$abcdefghijklmnopqrstuu";
  @Autowired private ReviewService reviewService;

  @Autowired private ReviewRepository reviewRepository;
  @Autowired private ReviewConverter reviewConverter;

  @Test
  @Transactional
  @Rollback
  public void reviewService_TestOfHashRevID_validateTimeTrue_recordExistAfter15M() {
    ReviewPatchPostDTO reviewPatchPostDTO = new ReviewPatchPostDTO();
    reviewPatchPostDTO.setComment("test20");
    reviewPatchPostDTO.setUserID(2);
    reviewPatchPostDTO.setRating(5);
    reviewPatchPostDTO.setClientName("delta");
    reviewPatchPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.100", fixedSalt));

    Review review = reviewConverter.createEntity(reviewPatchPostDTO);
    review.setCreatedAt(LocalDateTime.now().minusMinutes(15));
    reviewRepository.save(review);

    assertTrue(reviewService.validateTime(reviewPatchPostDTO));
  }

  @Test
  @Transactional
  @Rollback
  public void reviewService_TestOfHashRevID_validateTimeFalse_recordExistAfter5M() {
    ReviewPatchPostDTO reviewPatchPostDTO = new ReviewPatchPostDTO();
    reviewPatchPostDTO.setComment("test20");
    reviewPatchPostDTO.setUserID(2);
    reviewPatchPostDTO.setRating(5);
    reviewPatchPostDTO.setClientName("delta");
    reviewPatchPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.100", fixedSalt));

    Review review = reviewConverter.createEntity(reviewPatchPostDTO);
    review.setCreatedAt(LocalDateTime.now().minusMinutes(5));
    reviewRepository.save(review);

    assertFalse(reviewService.validateTime(reviewPatchPostDTO));
  }

  @Test
  @Transactional
  @Rollback
  public void reviewService_TestOfHashRevID_validateTimeTrue_NoRecord() {
    ReviewPatchPostDTO reviewPatchPostDTO = new ReviewPatchPostDTO();
    reviewPatchPostDTO.setComment("test20");
    reviewPatchPostDTO.setUserID(2);
    reviewPatchPostDTO.setRating(5);
    reviewPatchPostDTO.setClientName("delta");
    reviewPatchPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.105", fixedSalt));

    assertTrue(reviewService.validateTime(reviewPatchPostDTO));
  }
}
