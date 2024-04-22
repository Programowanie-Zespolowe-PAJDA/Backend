package umk.mat.pajda.ProjektZespolowy.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.Status;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Transactional
@Rollback
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
      "ngrok.link = link"
    })
public class ReviewServiceTestOfStats {
  @Autowired private UserRepository userRepository;

  @Autowired private ReviewRepository reviewRepository;

  @Autowired private ReviewService reviewService;

  @BeforeEach
  public void init() {
    User user = new User();
    user.setId(1000);
    user.setMail("test@gmail.com");
    user.setName("Adam");
    user.setSurname("Kowalski");
    user.setPassword("testpass123456789");
    user.setRole("ROLE_USER");
    userRepository.save(user);

    Review r1 = new Review();
    r1.setUser(userRepository.findByMail("test@gmail.com").get());
    r1.setRating(5);
    r1.setStatus(Status.COMPLETED);
    r1.setHashRevID("test");
    r1.setCreatedAt(LocalDateTime.now().minusMonths(1));
    r1.setId("100");
    reviewRepository.save(r1);

    Review r2 = new Review();
    r2.setUser(userRepository.findByMail("test@gmail.com").get());
    r2.setRating(1);
    r2.setStatus(Status.COMPLETED);
    r2.setHashRevID("test");
    r2.setCreatedAt(LocalDateTime.now().minusMonths(1));
    r2.setId("101");
    reviewRepository.save(r2);

    Review r3 = new Review();
    r3.setUser(userRepository.findByMail("test@gmail.com").get());
    r3.setRating(1);
    r3.setStatus(Status.COMPLETED);
    r3.setHashRevID("test");
    r3.setCreatedAt(LocalDateTime.now().minusMonths(1));
    r3.setId("102");
    reviewRepository.save(r3);

    Review r4 = new Review();
    r4.setUser(userRepository.findByMail("test@gmail.com").get());
    r4.setRating(2);
    r4.setStatus(Status.COMPLETED);
    r4.setHashRevID("test");
    r4.setCreatedAt(LocalDateTime.now().minusMonths(1));
    r4.setId("103");
    reviewRepository.save(r4);
  }

  @Test
  void reviewServiceTestGetNumberOfEachRatingsShouldReturnValid() {
    List<Integer> data = reviewService.getNumberOfEachRatings("test@gmail.com");
    assertTrue(data.get(1) == 2);
    assertTrue(data.get(2) == 1);
    assertTrue(data.get(5) == 1);
    assertTrue(data.get(0) == 0);
    assertTrue(data.get(3) == 0);
    assertTrue(data.get(10) == 0);
  }

  @Test
  void reviewServiceTestGetAvgRatingOfReviewReturnValid() {
    Double avgRating = reviewService.getAvgRatingOfReview("test@gmail.com").getAvgRating();
    assertTrue(avgRating == 2.25);
  }
}
