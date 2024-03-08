package umk.mat.pajda.ProjektZespolowy.Services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewPatchPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;

@SpringBootTest
@ActiveProfiles("tests")
public class ReviewServiceIntTestOfHashRevID {

  @Value("${FIXEDSALT_IPHASH:$2a$10$zxcvbhjklas}")
  private String fixedSalt;

  @Autowired private ReviewService reviewService;

  @Autowired private ReviewRepository reviewRepository;

  @Autowired private UserRepository userRepository;
  @Autowired private ReviewConverter reviewConverter;

  @Test
  @Transactional
  @Rollback
  public void reviewServiceTestOfHashRevIDValidateTimeTrueRecordExistAfter15M() {
    // Given
    User user = new User();
    user.setId(1000);
    user.setMail("test@gmail.com");
    user.setName("Adam");
    user.setSurname("Kowalski");
    user.setPassword("testpass123456789");
    user.setRole("ROLE_USER");
    userRepository.save(user);

    ReviewPatchPostDTO reviewPatchPostDTO = new ReviewPatchPostDTO();
    reviewPatchPostDTO.setComment("test20");
    reviewPatchPostDTO.setUserID(userRepository.findByMail("test@gmail.com").get().getId());
    reviewPatchPostDTO.setRating(5);
    reviewPatchPostDTO.setClientName("delta");
    reviewPatchPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.100", fixedSalt));

    Review review = reviewConverter.createEntity(reviewPatchPostDTO);
    review.setCreatedAt(LocalDateTime.now().minusMinutes(15));
    reviewRepository.save(review);

    // Then
    assertTrue(reviewService.validateTime(reviewPatchPostDTO));
  }

  @Test
  @Transactional
  @Rollback
  public void reviewServiceTestOfHashRevIDValidateTimeFalseRecordExistAfter5M() {
    // Given
    User user = new User();
    user.setId(1000);
    user.setMail("test@gmail.com");
    user.setName("Adam");
    user.setSurname("Kowalski");
    user.setPassword("testpass123456789");
    user.setRole("ROLE_USER");
    userRepository.save(user);

    ReviewPatchPostDTO reviewPatchPostDTO = new ReviewPatchPostDTO();
    reviewPatchPostDTO.setComment("test20");
    reviewPatchPostDTO.setUserID(userRepository.findByMail("test@gmail.com").get().getId());
    reviewPatchPostDTO.setRating(5);
    reviewPatchPostDTO.setClientName("delta");
    reviewPatchPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.100", fixedSalt));

    Review review = reviewConverter.createEntity(reviewPatchPostDTO);
    review.setCreatedAt(LocalDateTime.now().minusMinutes(5));
    reviewRepository.save(review);
    // Then
    assertFalse(reviewService.validateTime(reviewPatchPostDTO));
  }

  @Test
  @Transactional
  @Rollback
  public void reviewServiceTestOfHashRevIDValidateTimeTrueNoRecord() {
    // Given
    User user = new User();
    user.setId(1000);
    user.setMail("test@gmail.com");
    user.setName("Adam");
    user.setSurname("Kowalski");
    user.setPassword("testpass123456789");
    user.setRole("ROLE_USER");
    userRepository.save(user);

    ReviewPatchPostDTO reviewPatchPostDTO = new ReviewPatchPostDTO();
    reviewPatchPostDTO.setComment("test20");
    reviewPatchPostDTO.setUserID(userRepository.findByMail("test@gmail.com").get().getId());
    reviewPatchPostDTO.setRating(5);
    reviewPatchPostDTO.setClientName("delta");
    reviewPatchPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.105", fixedSalt));

    // Then
    assertTrue(reviewService.validateTime(reviewPatchPostDTO));
  }
}
