package umk.mat.pajda.ProjektZespolowy.services;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.misc.Status;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("tests")
@TestPropertySource(
    properties = {
      "FIXEDSALT_IPHASH = $2a$10$9elrbM0La5ooQgMP7i9yjO",
      "SHOP_ID = shop_id",
      "CLIENT_SECRET = client_secret",
      "CLIENT_ID = client_id",
      "profile = tests",
      "KEY_MD5 = key_md5",
      "ngrok.link = link",
            "GMAIL_APP_PASSWORD = gmail_app_password"
    })
public class ReviewServiceIntTestOfHashRevID {

  @Value("${FIXEDSALT_IPHASH}")
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

    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setComment("test20");
    opinionPostDTO.setUserID(userRepository.findByMail("test@gmail.com").get().getId());
    opinionPostDTO.setRating(5);
    opinionPostDTO.setClientName("delta");
    opinionPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.100", fixedSalt));

    Review review = reviewConverter.createEntity(opinionPostDTO, "fsad4234ffsda");
    review.setCreatedAt(LocalDateTime.now().minusMinutes(15));
    reviewRepository.save(review);

    // Then
    assertTrue(reviewService.validateTime(user, opinionPostDTO.getHashRevID()));
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

    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setComment("test20");
    opinionPostDTO.setUserID(userRepository.findByMail("test@gmail.com").get().getId());
    opinionPostDTO.setRating(5);
    opinionPostDTO.setClientName("delta");
    opinionPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.100", fixedSalt));

    Review review = reviewConverter.createEntity(opinionPostDTO, "fsad4234ffsda");
    review.setCreatedAt(LocalDateTime.now().minusMinutes(5));
    review.setStatus(Status.COMPLETED);
    reviewRepository.save(review);
    // Then
    assertFalse(reviewService.validateTime(user, opinionPostDTO.getHashRevID()));
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

    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setComment("test20");
    opinionPostDTO.setUserID(userRepository.findByMail("test@gmail.com").get().getId());
    opinionPostDTO.setRating(5);
    opinionPostDTO.setClientName("delta");
    opinionPostDTO.setHashRevID(BCrypt.hashpw("192.168.0.105", fixedSalt));

    // Then
    assertTrue(reviewService.validateTime(user, opinionPostDTO.getHashRevID()));
  }
}
